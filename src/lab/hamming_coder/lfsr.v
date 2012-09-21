// Hamming Code Encoder
module hamm_enc(out,in,reset);
parameter n=12,k=8;
output [n-1:0] out;
input [k-1:0] in;
input reset;
reg [n-1:0] out;
integer i,j;
always @(in or reset)
begin
if(reset)
out = 0;
else
begin
i=0; j=0;
while((i<n) || (j<k))
begin
while(i==0 || i==1 || i==3 || i==7)
begin
out[i] = 0;
i=i+1;
end
out[i] = in[j];
i=i+1;
j=j+1;
end
if( ^ (out & 11’b10101010101))
out[0] = ~out[0];
if(^(out & 11’b11001100110))
out[1] = ~out[1];
if(^(out & 11’b00001111000))
out[3] = ~out[3];
if(^(out & 11’b11110000000))
out[7] = ~out[7];
end
end
endmodule