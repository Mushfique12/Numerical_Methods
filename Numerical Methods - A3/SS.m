function f = SS(x, tolerance)
AUo = (1*10^(-2))*(1*10^(-2))*4*pi*(10^-7);
La = 0.5*(10^(-2));
Rg = La/AUo;
i = 0;
while (abs(NR(x,Rg)/NR(0,Rg)) > tolerance)
	i = i + 1;
	x = Sub(x); 
end
f = x;

function v = Sub(flux)
B = flux/(1/(100)^2);
X = [0.0,0.2,0.4,0.6,0.8,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9];
Y = [0.0, 14.7, 36.5, 71.7, 121.4, 197.4, 256.2,348.7,540.6,1062.8,2318.0,4781.9,8687.4,13924.3,22650.2];

rangeB = 0:.01:1.9;
Pol = PLI(X,Y,rangeB);
v = 8000/(3.978873577e7 + 0.3*polyval(Pol,B)/flux);