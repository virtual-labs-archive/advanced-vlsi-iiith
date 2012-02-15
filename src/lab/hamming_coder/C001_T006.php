<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Theory 
</div>
<br/>
</p>

<div class='def_cont'>
<h3>Calculating the Hamming Code</h3>
<div>
In mathematical terms, Hamming codes are a class of binary linear codes. For each integer <i>r >= 2</i>  there is a code with block length n =2<sup>r</sup> - 1 and message length k = 2<sup>r</sup> - r - 1. Hence the rate of Hamming codes is R = k / n = 1 - r / (2<sup>r</sup> - 1), which is highest possible for codes with distance 3 and block length 2<sup>r</sup> - 1. The parity-check matrix of a Hamming code is constructed by listing all columns of length r that are pairwise linearly independent.
<br /><br />
The key to the Hamming Code is the use of extra parity bits to allow the identification of a single error. Create the code word as follows:
<br /><br />
1.Mark all bit positions that are powers of two as parity bits. (positions 1, 2, 4, 8, 16, 32, 64, etc.)<br />
2.All other bit positions are for the data to be encoded. (positions 3, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 17, etc.)<br />
3.Each parity bit calculates the parity for some of the bits in the code word. The position of the parity bit determines the sequence of bits that it alternately checks and skips.<br />
Position 1: check 1 bit, skip 1 bit, check 1 bit, skip 1 bit, etc. (3,5,7,9,11,13,15,...)
<br />
     P1 = <b>XOR</b> of bits (3,5,7,9,11)
<br />
Position 2: check 2 bits, skip 2 bits, check 2 bits, skip 2 bits, etc. (3,6,7,10,11,14,15,...)
<br />
P2 = <b>XOR</b> of bits (3,6,7,10,11,14,15)
<br />
Position 4: check 4 bits, skip 4 bits, check 4 bits, skip 4 bits, etc. (4,5,6,7,12,13,14,15,20,21,22,23,...)
<br />
P4 = <b>XOR</b> of bits (5,6,7,12,13,14,15,20,21,22,23)
<br />
Position 8: check 8 bits, skip 8 bits, check 8 bits, skip 8 bits, etc. (8-15,24-31,40-47,...)
<br />
P8 = <b>XOR</b> of bits (9-15,24-31,40-47)
<br />
Position 16: check 16 bits, skip 16 bits, check 16 bits, skip 16 bits, etc. (16-31,48-63,80-95,...)
<br />
P16 = <b>XOR</b> of bits (17-31,48-63,80-95)
<br />
Position 32: check 32 bits, skip 32 bits, check 32 bits, skip 32 bits, etc. (32-63,96-127,160-191,...)
<br />
P32 = <b>XOR</b> of bits (33-63,96-127,160-191)
etc.
<br />
4.Set a parity bit to 1 if the total number of ones in the positions it checks is odd. Set a parity bit to 0 if the total number of ones in the positions it checks is even.
<br><br>

Here is an example:
<br>
A byte of data: 10011010<br>
Create the data word, leaving spaces for the parity bits: _ _ 1 _ 0 0 1 _ 1 0 1 0<br>
Calculate the parity for each parity bit (a ? represents the bit position being set):<br>
Position 1 checks bits 1,3,5,7,9,11:<br>
?_1_001_1010. Even parity so set position 1 to a 0:0_1_001_1010<br>
Position 2 checks bits 2,3,6,7,10,11:<br>
0?1_ 00 1_ 10 10. Odd parity so set position 2 to a 1: 011_ 001_ 1010<br>
Position 4 checks bits 4,5,6,7,12:<br>
011?0 0 1_1010. Odd parity so set position 4 to a 1: 0111001_1010<br>
Position 8 checks bits 8,9,10,11,12:<br>
0111001?1010. Even parity so set position 8 to a 0: 011100101010<br>
Code word: 011100101010.<br>
<center><img src ="Parity_Generator.bmp"></center>

</div>

</div>
<div class='def_cont'>
<br />
<h3>Finding and fixing an erroneous bit</h3>
<div>
When the bits are read from memory, they are checked again for possible errors. The parity is checked over the same combination of bits including the parity bit. The check bits are evaluated as follows:<br>
   C1 =  <b>XOR</b> of bits (1,3,5,7,9,11)<br><br>
   C2 =  <b>XOR</b> of bits (2,3,6,7,10,11,14,15)<br><br>
   C4 =  <b>XOR</b> of bits (4,5,6,7,12,13,14,15,20,21,22,23)<br><br>
   C8 =  <b>XOR</b> of bits (8-15,24-31,40-47)<br><br>
 C16 =  <b>XOR</b> of bits (16-31,48-63,80-95)<br><br>
 C32 =  <b>XOR</b> of bits (33-63,96-127,160-191)<br><br>
 etc.<br><br>
The above example created a code word of 011100101010. Suppose the word that was received was 011100101110 instead. Then the receiver could calculate which bit was wrong and correct it. The method is to verify each check bit. Write down all the incorrect parity bits. Doing so, you will discover that parity bits 2 and 8 are incorrect. It is not an accident that 2 + 8 = 10, and that bit position 10 is the location of the erroneous bit. In general, check each parity bit, and add the positions that are wrong, this will give you the location of the erroneous bit.
 The power and delay of the proposed 5-3 compressor has been compared with that of the standard 5-3 compressor found in literature. Table I shows the comparative study of power and delay of the proposed 5-3 compressor and the standard 5-3 compressor found in literature.
A comparative study of the simulation results presented in table 1 indicates that the proposed 5-3 compressor is more efficient in terms of power, delay and power-delay product than its peer design. 
<br/>
<br/>

</div></div>
</div>



