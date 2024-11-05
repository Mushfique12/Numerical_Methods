% Function to implement Lagrange Full-Domain polynomial 
function H = Lagrange(x,y,B)
n = length(x);
H = zeros(size(B));

for k = 1:n
    m = ones(size(B));
    for j = [1:k-1 k+1:n]
        m = (B-x(j))./(x(k)-x(j)).*m;
    end
    H = H + m*y(k);
end
