function V = newtonRaphson(E,R,V1,V2,Isa,Isb,k);  

f1 = V1 - E + R * Isa * (exp((V1 - V2) / 0.025) - 1.0);
f2 = (Isa * (exp((V1 - V2) / 0.025) - 1.0)) - (Isb * (exp(V2 / 0.025) - 1.0));   

f1V1Prime = 1.0 + (R * Isa) * (1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f1V2Prime = R * Isa * (- 1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f2V1Prime = Isa * (1.0 / 0.025) * (exp((V1 - V2) / 0.025));
f2V2Prime = (Isa * (- 1.0 / 0.025) * (exp((V1 - V2) / 0.025))) - (Isb * (1.0 / 0.025) * (exp(V2 / 0.025)));  

V = [V1 V2]';
f = [f1 f2]';
J = zeros(2);   
J(1,1) = f1V1Prime;
J(1,2) = f1V2Prime;
J(2,1) = f2V1Prime;
J(2,2) = f2V2Prime;

invJ = inv(J);
temp1 = invJ * f;
temp2 = - 1.0 * temp1;
V = V + temp2;