#include <bits/stdc++.h>

using namespace std;

void hilbert(int n)
{
    double m = 1.0;
    for (int i = 0; i < n; i++)
    {
        for (int j = i + 1; j <= n + i; j++)
        {
            printf("%.15f ", (m / j));
            // cout << (m / j) + 0.000000000000000 << " ";
        }
        if (i == 0)
        {
            cout << 1;
        }
        else
        {
            cout << 0;
        }
        cout << "\n";
    }
}

int main()
{
    hilbert(6);
    cout << "\n";
    hilbert(10);

    return 0;
}