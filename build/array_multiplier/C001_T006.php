<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Theory 
</div>	

<br/>
</p>

<div class='def_cont'>
<h3>Array Multiplier</h3>
<div>
<p><font size="3">
                     Digital multiplication entails a sequence of additions carried out on partial products.<br>
 <img src="Screenshot-1.png" width="700" height="400" >
</p>                                         
   
<p>
	<h2><b>Array Multiplier</b></h2><br>
	In Array multiplier partial products are independently computed in parallel.Let us consider two binary numbers
	A and B of m and n bits respectively,<br>                                        
        <img src="Screenshot.png" width="700" height="400" ><br>
	There are m*n summands that are produced in parallel by a set of
	m*n numbers of AND gates. <br>
	If <b>m = n </b>Then it will require <b>n(n-2)</b> full adders,<b> n </b>half-adders and <b>n*n</b>
	AND gates and worst case delay would be <b>(2n+1)td </b>.where td is the time delay of gates.
</p>
<p>
	Basic cell of a parallel array multiplier:-<br><br><br>
        <img src="Screenshot-2.png" width="300" height="200" >
</p>
<p>
<h2><b>Array structure of parallel multiplier</b></h2>
<br>   
    <img src="Screenshot-3.png" width="700" height="400" >
</p><br><br><br>
<p>
Consider computing the product of two 4-bit integer numbers given by A3A2A1A0
(multiplicand) and B3B2B1B0 (multiplier). The product of these two numbers can be
formed as shown below.<br>
<center><a href="#post"><img src="1.JPG" width = "800" height = "500"></a></center><br>
</p><br>
 Each of the ANDed terms is referred to as a partial product. The final product (the result)
is formed by accumulating (summing) down each column of partial products. Any carries
must be propagated from the right to the left across the columns.
<br>
Since we are dealing with binary numbers, the partial products reduce to simple AND
operations between the corresponding bits in the multiplier and multiplicand. The sums
down each column can be implemented using one or more 1-bit binary adders. Any adder
that may need to accept a carry from the right must be a full adder. If there is no
possibility of a carry propagating in from the right, then a half adder can be used instead,
if desired (a full adder can always be used to implement a half adder if the carry-in is tied
low). The diagram below illustrates a combinational circuit for performing the 4x4 binary
multiplication.
<br><br>
The initial layer of AND gates forms the sixteen partial products that result from ANDing
all combinations of the four multiplier bits with the four multiplicand bits. The column
sums are formed using a combination of half and full adders. Look again at the first two
illustrations of the binary multiplication process above, and make a careful comparison
with the figure below.
<br>

<center><a href="#post"><img src="2.JPG"></a></center><br>
The adder blocks (indicated by FA and HA) in the figure above are drawn in such a way
that the two bits to be added enter from the top, any carry in from the right enters from
the right, and any carry out exits from the left of each block. The output from the bottom
of a block is the sum.
<br><br>
The least significant output bit, S0 (the first column), involves only two input bits and is
computed as the simple output of an AND gate. This operation cannot generate a carry
out.
<br><br>
The next output bit, S1, involves the sum of two partial products. A half adder is used to
form the sum since there can be no carry in from the first column; however, a carry out
can be produced.
<br><br>
The third output bit, S2, is formed from the sum of three (1-bit) partial products plus a
possible carry in from the previous bit. This operation requires two cascaded adders (one
half adder and one full adder) to sum the four possible input bits (three partial products
and one possible carry in from the right).
<br><br>
The remaining output bits are formed similarly. Because in some columns we must add
more than two binary numbers, there may be more than one carry out generated to the
left.
<br>
</div>
</div>

<!--<div class='def_cont'>
<h3>Design Of Circuit</h3>
<br/>
<img src="experiment.png" width="950" height="600">
</div>-->
<div>

</div>
</div>



</div>
