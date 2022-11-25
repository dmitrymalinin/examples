#pragma once

class Receiver
{
	Receiver(const Receiver&){}
	Receiver& operator=(const Receiver&){return *this;}
public:

	Receiver(void)
	{
		hMapFile = NULL;
		pBuf = NULL;
	}

	~Receiver(void)
	{
	}

	void run()
	{
		hMapFile = NULL;
		pBuf = NULL;
		// создать или открыть CounterWriteEvent
		HANDLE hCounterWriteEvent = helper::CreateEvent(
			CounterWriteEventName, 
			TRUE, // BOOL ManualReset, 
			FALSE // BOOL InitialState
			);
		
		if (!hCounterWriteEvent)
		{
			helper::Print_sync(TEXT("Receiver: Could not create CounterWriteEvent.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return;
		}
	
		HANDLE hFileMappingMutex = helper::CreateMutex(FileMappingMutexName, FALSE);

		if (!hFileMappingMutex) 
		{
			helper::Print_sync(TEXT("Receiver: Could not create FileMappingMutex.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
			return;
		}

		helper::Print_sync(TEXT("Receiver: I am rinning\n"));

		// цикл чтения
		while(true)
		{
			// ждать, пока диспетчер не запишет значение счётчика
			DWORD WaitResult = WaitForSingleObject(hCounterWriteEvent, DispatcherTimeout*1000);  
			if (WaitResult == WAIT_OBJECT_0)
			{
				// прочитать значение счётчика
				if (!read(hFileMappingMutex)) break;
			} else if (WaitResult == WAIT_TIMEOUT)
			{
				helper::Print_sync(TEXT("Receiver: Wait CounterWriteEvent timed out.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());		
				break;
			} else
			{
				helper::Print_sync(TEXT("Receiver: Wait CounterWriteEvent failed.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());		
				break;
			}

		}

		CloseHandle(hCounterWriteEvent);
		if (hMapFile) 
		{
			if (pBuf) UnmapViewOfFile(pBuf);
			CloseHandle(hMapFile);
		}
		CloseHandle(hFileMappingMutex);
	}


private:

	HANDLE hMapFile;
	LPTSTR pBuf;

	bool read(HANDLE hFileMappingMutex)
	{
		if (hMapFile == NULL) // ещё не открыт
		{
			hMapFile = OpenFileMapping(
				FILE_MAP_READ,   // dwDesiredAccess
				FALSE,           // bInheritHandle,
				FileMappingName);

			if (!hMapFile) 
			{ 
				helper::Print_sync(TEXT("Receiver: Could not open file mapping object.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());
				return false;
			} 

			pBuf = helper::MapFile(hMapFile, FILE_MAP_READ, FileMappingBufSize);
			
			if (pBuf == NULL) 
			{ 
				helper::Print_sync(TEXT("Receiver: Could not map view of file.\n%s\n"), 
					helper::GetLastOSErrorMsg().c_str());
				return false;
			}
		}

		// ждать доступа к FileMapping
		DWORD WaitResult = WaitForSingleObject(hFileMappingMutex, INFINITE);
		if (WaitResult == WAIT_OBJECT_0 || WaitResult == WAIT_ABANDONED)
		{
			static clock_t prevReadTime = clock(); // Время предыдущего вызова read()
			double duration;
			duration = double(clock() - prevReadTime) / CLOCKS_PER_SEC;
			prevReadTime = clock();
			// напечатать значение счётчика
			helper::Print_sync(TEXT("Receiver: %s (%.2f)\n"), pBuf, duration);
		} else
		{
			helper::Print_sync(TEXT("Receiver: Wait FileMappingMutex failed.\n%s\n"), 
				helper::GetLastOSErrorMsg().c_str());
		}

		ReleaseMutex(hFileMappingMutex);

		return true;
	}
};
