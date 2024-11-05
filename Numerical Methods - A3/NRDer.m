function f = NRDer(flux, Rg)

B = flux/(1/(100)^2);
X = [-0.2,0.0,0.2,0.4,0.6,0.8,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0];
Y = [-14.7,0.0, 14.7, 36.5, 71.7, 121.4, 197.4, 256.2,348.7,540.6,1062.8,2318.0,4781.9,8687.4,13924.3,22650.2, 36574.6];
rangeB = 0:.01:1.9;

Pol = PLI(X,Y,rangeB);
changeInY = [];

for i = 2: length(rangeB)-1
    changeInY = [changeInY,(Pol(i+1) - Pol(i-1))/(rangeB(i+1) - rangeB(i-1))];
end

changeInY = [changeInY(1), changeInY];
changeInY = [changeInY,changeInY(length(rangeB)-1)];

f = Rg + (0.3*polyval(changeInY, B))/(1/100^2);