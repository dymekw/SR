
#ifndef SR_DEMO_ICE
#define SR_DEMO_ICE

//#include <Ice/BuiltinSequences.ice> 

module Demo
{
	exception RequestCanceledException
	{
	};
	
	enum Operation {Add, Sub, Mul, Dev, Mod, Pow};

	interface Calc
	{
		string compute(int a, int b, Operation op);
	};
};

#endif
