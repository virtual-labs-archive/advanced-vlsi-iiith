<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Introduction
</div>	

<br/>
</p>

<div class='def_cont'>
<h3>Ripple Carry Adder</h3>
<div>

<b>

I</b>t is possible to create a logical circuit using multiple full adders to add N-bit numbers. Each full adder inputs a Cin, which is the Cout of the previous adder. This kind of adder is a ripple carry adder, since each carry bit "ripples" to the next full adder. Note that the first (and only the first) full adder may be replaced by a half adder.

The layout of a ripple carry adder is simple, which allows for fast design time; however, the ripple carry adder is relatively slow, since each full adder must wait for the carry bit to be calculated from the previous full adder. The gate delay can easily be calculated by inspection of the full adder circuit. Each full adder requires three levels of logic. In a 32-bit [ripple carry] adder, there are 32 full adders, so the critical path (worst case) delay is 31 * 2(for carry propagation) + 3(for sum) = 65 gate delays.




</div>
</div>

<div class='def_cont'> 
<div>

</div>
</div>



</div>
