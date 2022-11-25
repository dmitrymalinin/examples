#pragma once

// счетчик
class Counter
{
	Counter(const Counter&){}
	Counter& operator=(const Counter&){return *this;}
public:
	explicit Counter(int init_val)
		:val(init_val)
	{

	}

	~Counter(void)
	{
	}

	int getVal() const
	{
		return val;
	}

	// указатель на буфер с текстовым представлением текущего значения счетчика
	char* getValBuf()
	{
		if (_itot_s(val, (LPTSTR)buf, sizeof(buf)/sizeof(TCHAR), 10) > 0)
		{
			//ошибка
			memset((void*)buf, 0, getValBufSize());
		}
		return buf;
	}

	// размер буфера
	DWORD getValBufSize() const
	{
		return sizeof(buf);
	}

	int operator++(int)
	{		
		return val++;
	}
public:
	int val;
	char buf[FileMappingBufSize];
};
