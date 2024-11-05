AUo = (1*10^(-2)*1*10^(-2)* 1.257*(10^(-6)));
N = 1000;
I = 8;
M = N*I;
La = 0.5*(10^(-2));
Lc = 30*(10^(-2));
Rg = La/AUo;
tolerance = 10^(-6); 
i = 0;
x = 0;

while (abs(NR(x,Rg)/NR(0,Rg)) > tolerance)
    i = i + 1;
    x = x - (NR(x, Rg)/NRDer(x,Rg));  
end

xSS = SS(1e-6, 1e-6);