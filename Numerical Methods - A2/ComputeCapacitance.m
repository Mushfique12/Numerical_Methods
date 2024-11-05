horizontalNodes = 6;
verticalNodes = 6;

mesh = zeros(horizontalNodes, verticalNodes);
input = zeros(4, 34);

% Reads Output File
file = fopen('output_file.txt', 'r');
input = fscanf(file, '%f', size(input));
input = input';
fclose(file);

% Stores the Values from the file
for x = 1: size(input, 1)
    nodeX = cast((input(x,2) / 0.02), 'int8');
    nodeY = cast((input(x,3) / 0.02), 'int8');
    mesh(nodeY + 1,nodeX + 1) = input(x,4);
end
	
% Voltage at inner core
mesh(1,1) = 15.0;
mesh(1,2) = 15.0;
voltageSquared = 225;
epsilon = 8.854187817620e-12;

% Calculates U^T x S x U 
netHalfEnergy = 0.0;
for y  = 1 : (verticalNodes - 1);
	for x = 1: (horizontalNodes - 1);
		u1 = mesh(y+1,x);
		u2 = mesh(y,x);
		u3 = mesh(y,x+1);
		u4 = mesh(y+1,x+1);
		
		netHalfEnergy = netHalfEnergy + (u1*u1 - u1*u2);
		netHalfEnergy = netHalfEnergy + (-u1*u4 + u2*u2);
		netHalfEnergy = netHalfEnergy + (-u2*u3 + u3*u3);
		netHalfEnergy = netHalfEnergy + (-u3*u4 + u4*u4);
    end
end

% Calculates the Capacitance per length
% Multiply be 4 to consider all the 4 quadrants
Capacitance = netHalfEnergy*(epsilon * 4 / voltageSquared);
