#include <stdio.h>
#include <math.h>

const int MAXN = 45000;

long long binary_search(int array[], int start, int end, long long element)
{

    int middle = (start + end) / 2;

    while (start <= end)
    {
        if (element == array[middle])
            return middle;
        else if (element < array[middle])
            return binary_search(array, start, middle - 1, element);
        else if (element > array[middle])
            return binary_search(array, middle + 1, end, element);
    }
    return -1;
}

int main()
{
    int n, m;
    long long total, ans, bs;

    scanf("%d %d", &n, &m);

    int houses[MAXN], orders[MAXN];

    for (int i = 0; i < n; ++i)
        scanf("%d", &houses[i]);

    for (int i = 0; i < m; ++i)
        scanf("%d", &orders[i]);

    total = 0, ans = 0;

    for (int i = 0; i < m; ++i)
    {
        bs = binary_search(houses, 0, n - 1, orders[i]);
        ans += abs(bs - total);
        total += (bs - total);
    }

    printf("%lld\n", ans);

    return 0;
}