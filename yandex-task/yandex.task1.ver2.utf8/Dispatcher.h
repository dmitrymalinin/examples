#pragma once

// Диспетчер
class Dispatcher
{
	Dispatcher(const Dispatcher&):cnt(0){}
	Dispatcher& operator=(const Dispatcher&){return *this;}
public:

	Dispatcher(
		int init_val, // начальное значение счётчика
		HANDLE _hMapFile, // сущ. FileMapping
		LPTSTR _pBuf, // отображенный буфер
		HANDLE _hIntervalTimer
	)
		:cnt(init_val), hMapFile(_hMapFile), pBuf(_pBuf),
		hIntervalTimer(_hIntervalTimer)
	{
		
	}

	~Dispatcher(void)
	{		
	}

	
	// запустить цикл увеличения счётчика
	void run(HANDLE hMapFile)
	{
		// создать CounterWriteEvent
		HANDLE hCounterWriteEvent = helper::CreateEvent(
			CounterWriteEventName, 
			TRUE, // BOOL ManualReset, 
			FALSE // BOOL InitialState
			);		

		if (!hCounterWriteEvent)
		{
			helper::Print_sync(TEXT("Dispatcher: Could not create counter event.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return;
		}		

		HANDLE hFileMappingMutex = helper::CreateMutex(FileMappingMutexName, FALSE);

		if (!hFileMappingMutex) 
		{
			helper::Print_sync(TEXT("Dispatcher: Could not create FileMappingMutex.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return;
		}

		
		helper::Print_sync(TEXT("Dispatcher: I am rinning\n"));

		while(true)
		{
			// ждать заданное время
			if (WaitForSingleObject(hIntervalTimer, DispatcherTimeout*1000) != WAIT_OBJECT_0)
			{
				helper::Print_sync(TEXT("Dispatcher: Wait IntervalTimer failed.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());
				break;
			}


			// увеличить счетчик
			cnt++;

			DWORD WaitResult = WaitForSingleObject(hFileMappingMutex, INFINITE);
			if (WaitResult == WAIT_OBJECT_0 || WaitResult == WAIT_ABANDONED)
			{
				// записать значение счётчика
				bool res = write();
				ReleaseMutex(hFileMappingMutex);
				if (!res) break;
			} else
			{
				ReleaseMutex(hFileMappingMutex);
				helper::Print_sync(TEXT("Dispatcher: Wait FileMappingMutex failed.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());		
				break;
			}

			// оповестить клиентов о записи нового значения
			if (!PulseEvent(hCounterWriteEvent)) 
		    {
				helper::Print_sync(TEXT("Dispatcher: PulseEvent failed.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());				
				break;
			}

			// ждать заданное время
			//Sleep(interval*1000); // в миллисекундах						
			// Вместо Sleep будем использовать WaitableTimer
		}
		
		CloseHandle(hFileMappingMutex);
		CloseHandle(hCounterWriteEvent);
	}

	// функция потока диспетчера
	static DWORD WINAPI Thread( LPVOID lpParam ) 
	{ 		
		int init_val = InitCounterValue;
		bool mapping_exists = false;


		// создать таймер для интервала
		HANDLE hIntervalTimer = CreateWaitableTimer(
			NULL,
			FALSE, // bManualReset
			DispatcherIntervalTimerName // lpTimerName
		);
		if (!hIntervalTimer) 
		{
			helper::Print_sync(TEXT("Dispatcher: Could not create IntervalTimer.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return 1;
		}
		LARGE_INTEGER DueTime; // Время, через которое состояние таймера будет установлено в signaled
		DueTime.QuadPart = 0LL;

		if (!SetWaitableTimer(
			hIntervalTimer, // hTimer
			&DueTime, // pDueTime,
			DispatcherInterval*1000, // lPeriod, в миллисекундах	
			NULL, // pfnCompletionRoutine,
			NULL, // lpArgToCompletionRoutine,
			FALSE // fResume
		))
		{
			helper::Print_sync(TEXT("Dispatcher: Could not SetWaitableTimer IntervalTimer.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return 1;
		}

		// захватить мютекс диспетчера
		HANDLE hDispatcherMutex = CreateMutex( 
			NULL,
			TRUE, // bInitialOwner 
			DispatcherMutexName);

		if (!hDispatcherMutex) 
		{
			helper::Print_sync(TEXT("Dispatcher: Could not create DispatcherMutex.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return 1;
		} else if (GetLastError() == ERROR_ALREADY_EXISTS) // открыт существующий DispatcherMutex
		{
			// ждать, пока не освободится
			helper::Print_sync(TEXT("Dispatcher: Wait for DispatcherMutex...\n"));

			WaitForSingleObject(hDispatcherMutex, INFINITE);
		}

				
		// создать новый или открыть сущ. FileMapping
		HANDLE hMapFile = CreateFileMapping(
			INVALID_HANDLE_VALUE,    
			NULL,                    // lpAttributes
			PAGE_READWRITE,          // flProtect
			0,                       // dwMaximumSizeHigh
			FileMappingBufSize,      // dwMaximumSizeLow
			FileMappingName);        

		if (!hMapFile) 
		{ 
			helper::Print_sync(TEXT("Dispatcher: Could not create file mapping object.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			ReleaseMutex(hDispatcherMutex);
			CloseHandle(hDispatcherMutex);
			return 1;
		} else if (GetLastError() == ERROR_ALREADY_EXISTS) // открыт существующий FileMapping
		{
			mapping_exists = true;
		}

		LPTSTR pBuf = helper::MapFile(hMapFile, FILE_MAP_ALL_ACCESS, FileMappingBufSize);

		if (!pBuf) 
		{ 
			helper::Print_sync(TEXT("Dispatcher: Could not map view of file.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			CloseHandle(hMapFile);
			ReleaseMutex(hDispatcherMutex);
			CloseHandle(hDispatcherMutex);
			return 1;
		}

		if (mapping_exists)
		{
			// прочитать текущее значение
			init_val = _tstoi(pBuf);			
		}

		// создать диспетчера
		Dispatcher disp(init_val, hMapFile, pBuf, hIntervalTimer);

		// запустить цикл
		disp.run(hMapFile);

		if (!UnmapViewOfFile(pBuf))
		{
			helper::Print_sync(TEXT("Dispatcher: Could not unmap view of file.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
		}
		CloseHandle(hMapFile);

		if (!ReleaseMutex(hDispatcherMutex)) 
        { 
			helper::Print_sync(TEXT("Dispatcher: Could not release DispatcherMutex.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
        } 

		CloseHandle(hDispatcherMutex);
		CloseHandle(hIntervalTimer);

		return 0; 
	} 
 

private:

	// записать значение счётчика
	bool write()
	{
		helper::Print_sync(TEXT("Dispatcher: %d\n"), cnt.getVal());			
		CopyMemory((PVOID)pBuf, cnt.getValBuf(), cnt.getValBufSize());

		return true;
	}
	

	Counter cnt; // счетчик
	HANDLE hMapFile;
	LPTSTR pBuf;
	HANDLE hIntervalTimer;
};
