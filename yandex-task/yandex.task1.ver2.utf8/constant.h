#pragma once

const int InitCounterValue = 0; // Начальное значение счётчика
const int DispatcherInterval = 1; // Интервал увеличения счетчика в секундах
const int DispatcherTimeout = 3; // Тайм-аут для диспетчера в секундах
const TCHAR DispatcherIntervalTimerName[] = TEXT("yandex.task1.ver2.DispatcherIntervalTimerName"); // Имя WaitableTimer


const TCHAR CounterWriteEventName[] = TEXT("yandex.task1.ver2.CounterWriteEventName"); // Имя Event, означающего конец записи в FileMapping
const TCHAR DispatcherMutexName[] = TEXT("yandex.task1.ver2.DispatcherMutexName"); // Имя Mutex, означающего что диспетчер запущен

const TCHAR FileMappingName[] = TEXT("yandex.task1.ver2.FileMappingObject"); // Имя FileMapping
const int StrValueSize = 12; // Размер строки для десятичного представления int в символах (знак + 10 симв int + 0 ) 
const DWORD FileMappingBufSize = StrValueSize*sizeof(TCHAR); // Максимальный размер FileMapping в байтах
const TCHAR FileMappingMutexName[] = TEXT("yandex.task1.ver2.FileMappingMutexName"); // Имя Mutex для сихронизации доступа к FileMapping
