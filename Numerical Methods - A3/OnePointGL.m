N = [];
x = 0:0.01:1;
errorList = [];
intValList = [];

% Calculating the actual integral value for the given function
format long;
%funct = @(x)(cos(x));
funct = @(x)(log(x));
actualValue = integral(funct,0,1);

% Calculating using One-Point Gauss-Legendre integration
for i = 1:1:20
   N = [N, i];
   integrationVal = integration(funct, 0.0 , 1.0, i);
   intValList = [intValList, integrationVal];
   error = abs(integrationVal - actualValue);
   errorList = [errorList, error];
end
errorList = errorList';
intValList = intValList';

figure;
plot(log10(N),log10(errorList), 'LineWidth', 3);
title('Error vs N');
xlabel('log(N)');
ylabel('log(E)');
grid on;
