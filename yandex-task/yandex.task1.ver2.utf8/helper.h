#pragma once

#ifdef UNICODE
	typedef std::wstring std_string;
#else
	typedef std::string std_string;
#endif

class CSLock
{
public:
	CSLock(CRITICAL_SECTION& _cs)
		:cs(_cs)
	{
		EnterCriticalSection(&cs);
	}
	~CSLock()
	{
		LeaveCriticalSection(&cs);
	}
private:
	CRITICAL_SECTION& cs;
};

// вспомогательные функции
class helper
{
public:
	static CRITICAL_SECTION PrintCriticalSection;

	// получить текст ошибки
static std_string GetOSErrorMsg(DWORD dwError)
{
	std_string res;
	LPTSTR MessageBuffer;

	if (dwError == 0) return res;

	HMODULE hModule = NULL;

	DWORD dwFormatFlags = FORMAT_MESSAGE_ALLOCATE_BUFFER |
		FORMAT_MESSAGE_IGNORE_INSERTS |
		FORMAT_MESSAGE_FROM_SYSTEM ;


	if (FormatMessage(
		dwFormatFlags,
		hModule, 
		dwError,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
		(LPTSTR)&MessageBuffer,
		0,
		NULL
		))
	{
		res = MessageBuffer;
		LocalFree(MessageBuffer);
	} else
		res = TEXT("Error in FormatMessage()");

	return res;
}

// получить текст последней ошибки
static std_string GetLastOSErrorMsg()
{
	return helper::GetOSErrorMsg(GetLastError());
}

// последовательный вывод
static int Print_sync(LPCTSTR msg, ...)
{
	CSLock csLock(PrintCriticalSection);
	//EnterCriticalSection(&PrintCriticalSection);

	int res;
	
	va_list arglist;
	va_start(arglist, msg);

	res = _vtprintf(msg, arglist);

	va_end(arglist);

	//LeaveCriticalSection(&PrintCriticalSection);

	return res;
}

// создать Event
static HANDLE CreateEvent(LPCTSTR EventName, BOOL ManualReset, BOOL InitialState)
{
	HANDLE hEvent;

	
	hEvent = ::CreateEvent(
		NULL,
		ManualReset, // bManualReset,
		InitialState, // bInitialState,
		EventName
		);
	if (!hEvent)
	{
		helper::Print_sync(TEXT("helper: Could not create Event.\n%s\n"), 
			helper::GetLastOSErrorMsg().c_str());
	}

	
	return hEvent;
}

// отобразить File в адресн. пр-во процесса
static LPTSTR MapFile(HANDLE hMapFile, DWORD DesiredAccess, SIZE_T NumberOfBytesToMap)
{
	LPTSTR pBuf;
	pBuf = (LPTSTR) MapViewOfFile(hMapFile,
		DesiredAccess, 
		0,                   
		0,           // offset        
		NumberOfBytesToMap);           

	if (!pBuf) 
	{ 
		helper::Print_sync(TEXT("helper: Could not map view of file.\n%s\n"), 
			helper::GetLastOSErrorMsg().c_str());		
	}

	return pBuf;
}

// создать мютекс
static HANDLE CreateMutex(LPCTSTR MutexName, BOOL InitialOwner)
{
	HANDLE hMutex;

	
	hMutex = ::CreateMutex( 
		NULL,
		InitialOwner, // bInitialOwner 
		MutexName);

	if (!hMutex) 
	{
		helper::Print_sync(TEXT("helper: Could not create Mutex.\n%s\n"), 
			helper::GetLastOSErrorMsg().c_str());
	}

	
	return hMutex;
}

}; // class helper