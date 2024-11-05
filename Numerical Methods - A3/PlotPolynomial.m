% First six points
X = [0.0, 0.2, 0.4, 0.6, 0.8, 1.0];
Y = [0.0, 14.7, 36.5, 71.7, 121.4, 197.4];
B = 0:.001:1.9;
H = Lagrange(X,Y,B);
figure;
plot(H,B, 'LineWidth', 3);
title('B vs H');
xlabel('H(A/m)');
ylabel('B(T)');
grid on;

% Using the other six points
X = [0, 1.3, 1.4, 1.7, 1.8, 1.9];
Y = [0.0, 540.6, 1062.8, 8687.4, 13924.3, 22650.2];
H = Lagrange(X,Y,B);
figure;
plot(H,B, 'LineWidth', 3);
title('B vs H');
xlabel('H(A/m)');
ylabel('B(T)');
grid on;