<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Theory 
</div>
<br/>
</p>


<div class='def_cont'>
<div>
The circuit design for LFSR requires the D-Flipflops and XOR Gates. XOR-Gates are used for generating the linear polynomial for feedback. The linear polynomial is nothing but weighted modulo-2 addition of the outputs of the D-Flipflops. This modulo-2 sum is fed back into the Most Significant bit of the register.
<br />
Modulo-2 addition is a sum in which output can be only 1 and 0 i.e.  modulo-2 addition of 3 and 2 is 1. For the remaining text <b>+</b> will denote the modulo-2 addition.
<br />
The possible weights are only <b>0</b> or <b>1</b> as we are working with binary. One sample linear feedback polynomial is shown below:
<br/>
<center><img src="Sample_Poly.JPG"></center>
<br/>
Here Superscripts show the D-Flipflop whose output is included in Polynomial. The above shown Polynomial is a valid polynomial for any LFSR whose size is greater than 13 bits.
<br />
As we are dealing only with <b>1</b> and <b>0</b> XOR gate can do the job of a Modulo-2 adder as clear from the truth table shown below.
<br/>

</div>

<div>
<br/>
<center><img src="1ttxor1.gif" align="centre"></center>
<br/>
Mathematically modulo-2 addition is  z =(x+y)%2.
</div>

<div>
Following Figure shows the working of a 4-bit LFSR with polynomial X^1 + X^0. [Here ^ denotes the superscript.]
<br />
<br />
<center><img src="LFSR-F4.GIF"></center>
<br />
<br />
The left figure is state diagram for the LFSR shown on right.
<br />
All polynomials may not give the complete sequence. Complete sequence is a sequence which covers all the possible binary sequences possible. For instance in a 4-bit LFSR there are in total 16 sequences possible but it depends on the seed and Feedback polynomial how many sequences will come in final output. Here Seed is the first sequence from which the LFSR starts.
<br />
There are some Polynomials for which the complete sequence will appear irrespective of seed.
Such Polynomials are called as <b><i>Maximal Polynomial</i></b> and the LFSRs we get from those polynomials are called as <b><i>Maximal LFSRs</i></b>.
Following Table shows the Maximal Polynomials for LFSRs of 2 to 19 bits. 
<center><img src="IMAGE_TABLE_POLYNOMIAL.JPG"></center>
Here <b>1</b> is included just to make it sure that for 2 D-Fliflops in a polynomial we require 2 XOR gates. You can remove that <b>1</b> from Polynomial without loss of generality.
<br />
This circuit is totally synchronous circuit controlled by one single clock. All clocks shown in the workspace are perfectly synchronized.
<br />
</div>

</div>



