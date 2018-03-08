fileID = fopen('SnakeConverge.txt','r');
formatSpec = '%f';
A = fscanf(fileID,formatSpec);

Score = [];
tScore = [];

Gen = [];
tGen = [];

Median = [];
Median2 = [];
Average = [];
VarAvg = [];
Max = [];

Var = [];

Scoreavg = [];
avg = 0;

for x = 1:size(A)
    if mod(x,2) == 1
        tGen = [tGen,A(x)];
    end
    if mod(x,2) == 0
        tScore = [tScore,A(x)];
    end
    if mod(x,52) == 0
        Score = [Score,tScore];
        Gen = [Gen,tGen];
        Median = [Median,quantile(tScore,0.8)];
        Median2 = [Median2,quantile(tScore,0)];
        Average = [Average,mean(tScore)];
        Max = [Max,max(tScore)];
        tScore = [];
        tGen = [];
    end
    
end



%scatter(Gen,Score,4,'filled');
hold on
title(max(Score));
%plot(1:300,Median);
plot(1:300,Average);
plot(1:300,Max);
plot(1:300,Median2)

%errorbar(1:15:300,VarAvg,Var)
hold off


max(Score)









