b = 1.0;
a = 0.0;
relativeWidths = [1,2,4,8,16,32,64,128,256,512];
scale = (b - a) / sum(relativeWidths);
widths = [];

% Calculating the actual integral value for the given function
format long;
funct = @(x)(log(x));
actualValue = integral(funct,0,1);

for i = 1:length(relativeWidths)
    width = (relativeWidths(i) * scale);
    widths = [widths, width];
end

runningWidth = 0;
summation = 0;

for i = 1: length(widths)
    summation = summation + log((widths(i)/2) + runningWidth) * widths(i);
    runningWidth = runningWidth + widths(i);
end

errorUnequal = abs(summation - actualValue);