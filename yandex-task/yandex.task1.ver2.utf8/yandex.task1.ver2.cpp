// yandex.task1.ver2.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"


int _tmain(int argc, _TCHAR* argv[])
{
	InitializeCriticalSection(&helper::PrintCriticalSection);

	

	// создать поток для диспетчера
	DWORD DispThreadId;
	HANDLE hDispThread = CreateThread(
		NULL,
		0, 
		Dispatcher::Thread,
		NULL, // lpParameter
		0,	// dwCreationFlags
		&DispThreadId // lpThreadId
	);

	if (!hDispThread)
	{
		helper::Print_sync(TEXT("main: Could not create thread for dispatcher.\n%s\n"), 
			helper::GetLastOSErrorMsg().c_str());
		DeleteCriticalSection(&helper::PrintCriticalSection);	
		return 1;
	}

	// запустить клиента
	Receiver recv;
	recv.run();

	
	CloseHandle(hDispThread);

	
	helper::Print_sync(TEXT("Press any key to exit\n"));
	_getch();

	DeleteCriticalSection(&helper::PrintCriticalSection);	

	return 0;
}

