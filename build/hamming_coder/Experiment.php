<!DOCTYPE HTML>
<html>
<head>
<title>Virtual Labs - IIIT Hyderabad</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
-->
</style>
<style type="text/css">@import "flexnav.css";</style>
<script type="text/javascript" src="js/beethoven.js"></script>
<link href="default.css" rel="stylesheet" type="text/css">
<link href="custom.css" rel="stylesheet" type="text/css">


<script type="text/javascript" src="ddtabmenu.js">

</script>

<link rel="stylesheet" type="text/css" href="glowtabs.css" />



</head>
<body bgcolor="#FFFFFF">
<div id="header_main"><img src="images/header_01.jpg" align="right" style="padding-right:0px"></div>
<div id="no_print">
<!-- start header -->
<div id="header">
<ul id="menuTop">

<li><a href="http://virtual-labs.ac.in/labs/cse13" target="_self">Home</a></li><li><a href="#" target="_self">FAQ</a></li><li><a href="#" target="_self">Contact us</a></li><li><a href="http://vlab.co.in" target="_self">Vlab.co.in</a></li>		
</ul>

</div>


<div style="position: relative; margin:auto; width: 1024px; background-color:#e3f2fc"><br/>


<span class="title"></span>
</div>
<div id="no_print">

</div>
<div style="background-image:url(images/content_bg.jpg);position: relative; margin:auto; width: 1024px">

<div id="contentBox" style=" padding:0px; padding-left:50px; padding-right:50px;">
<br>
<div id="ddtabs2" class="glowingtabs">
<ul>
<li><a href="Experiment.php?tid=T001&code=C001"><span><center><img src="images/intro.jpeg" BORDER=0 width=58px height=48px><br>Introduction</center></span></a></li>

<li><a href="Experiment.php?tid=T002&code=C001"><span><center><img src="images/object.jpeg" BORDER=0 width=58px height=48px><br>Objective</center></span></a></li>
<li><a href="Experiment.php?tid=T003&code=C001"><span><center><img src="images/manual.jpeg" BORDER=0 width=58px height=48px><br>Manual</center></span></a></li>
<li><a href="Experiment.php?tid=T004&code=C001"><span><center><img src="images/pro.jpg" BORDER=0 width=58px height=48px><br>Procedure</center></span></a></li>
<li><a href="Experiment.php?tid=T005&code=C001"><span><center><img src="images/sim.jpg" BORDER=0 width=58px height=48px><br>Virtual Experiment</center></span></a></li>
<li><a href="Experiment.php?tid=T006&code=C001"><span><center><img src="images/theory.jpg" BORDER=0 width=58px height=48px><br>Theory</center></span></a></li>
<li><a href="Experiment.php?tid=T007&code=C001"><span><center><img src="images/quiz.jpeg" BORDER=0 width=58px height=48px><br>Quiz</center></span></a></li>
<li><a href="Experiment.php?tid=T008&code=C001"><span><center><img src="images/books.jpg" BORDER=0 width=58px height=48px><br>References</center></span></a></li>
</ul>

</div>
<?php
$tid=$_GET['tid'];
$code=$_GET['code'];
$fname="";
if($tid=="")
{
$tid="T001";
}
if($code=="")
{
$code="C001";
}
$fname=$code."_".$tid.".php";
if(file_exists($fname))
{
include($fname);
}
else
{
include("Error.php");
}
?>
</div>

</div>



</div>

<div style="position: relative; margin:auto;margin-bottom:10px; width: 1024px; background-color:#0e8de0"><img src="images/footer-curve.jpg" width="1024" height="31" alt=""><div class="copyright">Copyright &copy; 2010-2011</div><br>
</div>

</body>
</html>

