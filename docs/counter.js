// var canvas;
// var score;
// var button;

// function setup(){
// 	canvas = createCanvas(100,100);
// 	score = 0;
// 	createP('Click the button to add the token number.')
// 	button = createButton('click');	
// 	button.mousePressed(increaseScore);
// }

// function increaseScore(){
// 	score++;
// }

// function draw(){
// 	background(0);
// 	textAlign(CENTER);
// 	textSize(32);
// 	fill(255);
// 	text(score, width/2, height/2);
// }

var animate = setInterval(function(){update()},1)

var clicks = 0;
function increment(){
    clicks++;
    redraw();
};
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
console.log(ctx);
function redraw(){
    ctx.clearRect(0, 0, c.width, c.height);
    ctx.font="16px Verdana";
    ctx.fillText("Score: " + clicks,190,20);
}
redraw();	