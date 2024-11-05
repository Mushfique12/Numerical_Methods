% Setting Initial Circuit Parameters
E = 0.22;    
R = 500.0;
Isa = 0.0000006;
Isb = 0.0000012;
V1 = 0.0;
V2 = 0.0;
Ek = 10^(-6);

% Initiating the list of v, f1 and f2
format long;
k = 0;
Vlist = [];
f1list = [];
f2list = [];

V = newtonRaphson(E,R,V1,V2,Isa,Isb);
Vlist = [Vlist, V];
f1 = V(1,1) - E + R * Isa *(exp((V(1,1) - V(2,1))/0.025) - 1.0);
f1list = [f1list, f1];

while (f1 > Ek)
    k = k + 1;
    V = newtonRaphson(E,R,V(1,1),V(2,1),Isa,Isb);
    Vlist = [Vlist, V];
    f1 = V(1,1) - E + R * Isa *(exp((V(1,1) - V(2,1))/0.025) - 1.0);
    f2 = Isa * ((exp((V(1,1) - V(2,1)) / 0.025) - 1.0)) - Isb * (exp(V(2,1) / 0.025) - 1.0) ;
    f1list = [f1list, f1];
    f2list = [f2list, f2];
end

% Generating the List of f1, f2, v1, v2 and k
Vlist = Vlist';
f1list = f1list';
f2list = f2list';
klist = 1:k+1;
klist = klist';

% Plotting the Error Function
figure; 
plot(klist,f1list, 'LineWidth', 3);
title('f1 vs Number of iterations (k)');
xlabel('k');
ylabel('f1');
grid on;