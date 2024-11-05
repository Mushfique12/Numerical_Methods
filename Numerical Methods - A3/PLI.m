function Pol = PLI(x,y,u)
% PLI - Piecewise linear interpolation.
% Pol = piecelin(x,y,u) finds piecewise linear L(x)
% L(x(j)) = y(j) and v(k) = L(u(k)).
% First divided difference
delta = diff(y)./diff(x);
% Find subinterval indices k so that x(k) <= u <x(k+1)
n = length(x);
m = ones(size(u));
for j = 2:n-1
    m(x(j) <= u) = j;
end
% Evaluate interpolant
s = u - x(m);
Pol = y(m) + s.*delta(m);