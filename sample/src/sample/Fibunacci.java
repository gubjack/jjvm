
package sample;


class  Fibunacci
{

    static int  straight (int MAX)
    {
        int  first = 0;
        int  second = 1;
        
        for (int  i = 2;  i <= MAX;  i++)
        {
            int  sum = first + second;
            first = second;
            second = sum;
        }
        return second;
    }
    static int  recursive (int a)
    {
        if (a == 1  ||  a == 2)
            return 1;
        return recursive (a - 1)
                + recursive (a - 2);
    }

}
