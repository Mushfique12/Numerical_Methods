function V = newtonRaphson(E,R,V1,V2,Isa,Isb);  
format long;
% Calculating f1 and f2
f1 = V1 - E + R * Isa * (exp((V1 - V2) / 0.025) - 1.0);
f2 = (Isa * (exp((V1 - V2) / 0.025) - 1.0)) - (Isb * (exp(V2 / 0.025) - 1.0));   

% Calculating the Derivatives for the Jacobian Matrix
f1V1 = 1.0 + (R * Isa) * (1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f1V2 = R * Isa * (- 1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f2V1 = Isa * (1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f2V2 = (Isa * (- 1.0 / 0.025) * (exp((V1 - V2) / 0.025))) - (Isb * (1.0 / 0.025) * (exp(V2 / 0.025)));  

% Setting the V and f matrices
V = [V1 V2]';
f = [f1 f2]';

% Setting the Values for the Jacobian Matrix
J = zeros(2);   
J(1,1) = f1V1;
J(1,2) = f1V2;
J(2,1) = f2V1;
J(2,2) = f2V2;

% Calculating the V Matrix (V1 and V2)
invJ = inv(J);
temp1 = invJ * f;
temp2 = - 1.0 * temp1;
V = V + temp2;