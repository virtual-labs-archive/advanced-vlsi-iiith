<div id='Container'>
<br/>
<br/>
<p class="tag_title">
<div class='tab_heading' > Theory 
</div>	

<br/>
</p>

<div class='def_cont'>
<h3>Description of how Clock Gating works</h3>
<div>
<b>CLOCK GATING</b> is one of the power-saving techniques used on many synchronous circuits.<br />
To save power, clock gating technique adds more logic to a circuit to prune the clock tree,thus disabling portion of the circuitry so that its flip-flops do not change state : their switching power consumption goes to zero, and only leakage currents are incurred.<br/>
<br/>
Clock gating works by taking the enabled conditions attached to registers and uses them to gate the clocks. However a design must contain these enabled conditions in order for clock gating to occur.This clock gating process can save significant area as well as power, since it removes large numbers of muxes and replaces them with clock gating cells.This clock gating logic is generally in the form of "Integrated Clock Gating" (ICG) cells.However, note that the clock gating logic will change the clock tree structure, since the clock gating logic will sit on clock tree. </div>
</div>

</div>
<div class='def_cont'>
<h3>Adding clock gating logic into design</h3>
<div>
There are a number of ways in which clock gating logic can be added into a design.They are as follows. <br /><br />
<ol>
<li>Coded into the RTL code as enable conditions that can be automatically translated into clock gating logic by synthesis tools(fine grain clock gating).</li>
<li>Inserted into the design manually by the RTL designers(typically as module level clock gating) by instantiating library specific ICG(Integrated Clock Gating) cells to gate the clocks of specific modules or registers.</li>
<li>Semi-automatically inserted into the RTL by automated clock gating tools.These tools either insert ICG cells into the RTL, or add enable conditions into the RTL code.These typically also offer sequential clock gating optimisations.</li>
</ol><br />
Although asynchronous circuits by definition do not have a "clock", the term <b>perfect clock gating</b> is used to illustrate how various clock gating techniques are simply approximations of the data-dependent behavior exhibited by asynchronous circuitry.As the granularity on which you gate the clock of a synchronous circuit approaches zero, the power consumption of that circuit approaches that of an asynchronous circuit : the circuit only generates logic transitions when it is actively computing.
<br /><br />
</div></div>
<div class='def_cont'>
<h3><!-- Other Details--></h3><br/> 
<div>

</div>
</div>



</div>
