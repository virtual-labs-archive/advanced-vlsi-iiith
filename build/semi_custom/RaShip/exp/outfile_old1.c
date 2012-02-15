#include<stdio.h>
main()
{
	printf("Title:\n");
	printf("*inverter\n");
	printf("Date:\n");
	printf("Thu\n");
	printf("Nov\n");
	printf("11\n");
	printf("09:30:47\n\n");

	printf("2010\n");
	printf("Plotname:\n");
	printf("Transient\n");
	printf("Analysis\n");
	printf("Flags:\n");
	printf("real\n");
	printf("No.\n");
	printf("Variables:\n");
	printf("5\n");
	printf("No.\n");
	printf("Points:\n");
	printf("20\n\n\n\n\n");





	printf("Variables:\n\n");

	printf("0\n");
	printf("A\n");
	printf("A\n\n");

	printf("1\n");
	printf("B\n");
	printf("B\n\n");

	printf("2\n");
	printf("C_in\n");
	printf("C\n");
	printf("Values:\n");
	int counter=0;
	while(counter<20)
	{
		printf("%d\n\n",counter);
		if(counter%4==0 || counter % 4==1)          //A
			printf("1.01e+00\n\n");
		else
			printf("0.01e+00\n\n");
		if(counter%8 == 2 || counter % 8  == 3 || counter % 8 == 4 || counter % 8 == 5)                         //B
			printf("1.01e+00\n\n");
		else
			printf("0.01e+00\n\n");
		if(counter%8 < 4 )          //Sum
			printf("1.01e+00\n\n");
		else
			printf("0.01e+00\n\n");
		if(counter % 8== 4 || counter % 8 ==5)                           //carry
			printf("1.01e+00\n");
		else
			printf("0.01e+00\n");
		counter++;
	}
}
