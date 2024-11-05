function f = integration(funct, a, b, n)
segSize = (b - a) / n;
summation = 0;

for i = 1:n
    summation = summation + funct(segSize*((i-1) + 0.5));
end
f = summation * segSize;