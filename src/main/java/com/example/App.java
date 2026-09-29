package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {

    @GetMapping(value = "/", produces = "text/html")
    public String home() {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title> Dragon </title>
<style>
*{margin:0;padding:0;box-sizing:border-box}
html,body{height:100%;background:#020818;overflow:hidden}
canvas{display:block;width:100vw;height:100vh}
.t{position:fixed;top:4vh;width:100%;text-align:center;font:800 clamp(1.3rem,4vw,3rem) system-ui,sans-serif;letter-spacing:.5em;color:#bfe9ff;text-shadow:0 0 20px #2a8cff,0 0 50px #1c55e8;pointer-events:none;opacity:.85}
.h{position:fixed;bottom:3vh;width:100%;text-align:center;font:.9rem system-ui,sans-serif;color:#7fbfff;opacity:.6;pointer-events:none}
.v{position:fixed;inset:0;pointer-events:none;background:radial-gradient(ellipse at center,transparent 55%,rgba(0,5,25,.75))}
</style>
</head>
<body>
<canvas id="c"></canvas>
<div class="v"></div>
<div class="t"> DRAGON </div>
<div class="h">Click or tap to unleash extra blue fire</div>
<script>
const c=document.getElementById('c'),x=c.getContext('2d');
let W,H,T=0,last=performance.now(),force=0;
function rs(){const d=Math.min(devicePixelRatio||1,2);W=innerWidth;H=innerHeight;c.width=W*d;c.height=H*d;x.setTransform(d,0,0,d,0,0);}
addEventListener('resize',rs);rs();
addEventListener('pointerdown',()=>force=150);

const stars=[...Array(140)].map(()=>({a:Math.random(),b:Math.random()*.7,r:Math.random()*1.5+.3,p:Math.random()*6}));
const cols=['232,255,255','155,233,255','76,195,255','42,140,255','28,85,232','26,47,176','16,26,112'];
const SPR=cols.map(k=>{const s=document.createElement('canvas');s.width=s.height=64;const g=s.getContext('2d'),r=g.createRadialGradient(32,32,0,32,32,32);
r.addColorStop(0,'rgba('+k+',1)');r.addColorStop(.45,'rgba('+k+',.55)');r.addColorStop(1,'rgba('+k+',0)');g.fillStyle=r;g.fillRect(0,0,64,64);return s;});
let P=[];

const R=(a,b,t)=>[a*Math.cos(t)-b*Math.sin(t),a*Math.sin(t)+b*Math.cos(t)];
function poly(pts,f){x.fillStyle=f;x.beginPath();pts.forEach((p,i)=>i?x.lineTo(p[0],p[1]):x.moveTo(p[0],p[1]));x.closePath();x.fill();}

function mount(base,amp,col,sd){x.fillStyle=col;x.beginPath();x.moveTo(0,H);
for(let i=0;i<=W+16;i+=16)x.lineTo(i,H*base-Math.abs(Math.sin(i*.004+sd))*amp*H-Math.sin(i*.011+sd*2)*amp*H*.3);
x.lineTo(W,H);x.fill();}

function background(){
const g=x.createLinearGradient(0,0,0,H);g.addColorStop(0,'#01040f');g.addColorStop(.6,'#0a1f52');g.addColorStop(1,'#12327a');
x.fillStyle=g;x.fillRect(0,0,W,H);
stars.forEach(s=>{x.fillStyle='rgba(200,230,255,'+(.4+.6*Math.sin(T*2+s.p))*.8+')';x.beginPath();x.arc(s.a*W,s.b*H,s.r,0,7);x.fill();});
const m=x.createRadialGradient(W*.8,H*.18,0,W*.8,H*.18,140);m.addColorStop(0,'rgba(190,225,255,.5)');m.addColorStop(1,'rgba(190,225,255,0)');
x.fillStyle=m;x.fillRect(W*.8-140,H*.18-140,280,280);
x.fillStyle='#dcefff';x.beginPath();x.arc(W*.8,H*.18,40,0,7);x.fill();
mount(.82,.14,'#0b1f4d',1);mount(.92,.1,'#06122e',4);
}

function wing(sx,sy,ph,sc,fl,ln){
const th=-1.1+Math.sin(T*4.2-ph)*1.1,lg=Math.sin(T*4.2-ph-1)*.55,E0=[-10,-90];
const Ep=R(E0[0],E0[1],th);
const hd=q=>{const r=R(q[0]-E0[0],q[1]-E0[1],th+lg);return[sx+(Ep[0]+r[0])*sc,sy+(Ep[1]+r[1])*sc];};
const E=[sx+Ep[0]*sc,sy+Ep[1]*sc],Wr=hd([-30,-170]),tp=[[-15,-300],[-105,-275],[-170,-205],[-195,-120]].map(hd);
const b=R(-150,25,th*.35),B=[sx+b[0]*sc,sy+b[1]*sc];
x.beginPath();x.moveTo(sx,sy);x.lineTo(E[0],E[1]);x.lineTo(Wr[0],Wr[1]);x.lineTo(tp[0][0],tp[0][1]);
for(let k=0;k<3;k++){const a=tp[k],d=tp[k+1];x.quadraticCurveTo((a[0]+d[0])/2*.8+Wr[0]*.2,(a[1]+d[1])/2*.8+Wr[1]*.2,d[0],d[1]);}
x.quadraticCurveTo((tp[3][0]+B[0])/2,(tp[3][1]+B[1])/2,B[0],B[1]);x.closePath();
x.fillStyle=fl;x.fill();x.strokeStyle=ln;x.lineWidth=4;x.lineJoin='round';x.stroke();
x.lineWidth=2.5;x.beginPath();tp.forEach(t=>{x.moveTo(Wr[0],Wr[1]);x.lineTo(t[0],t[1]);});x.stroke();
}

const N=24,ST=17;
function body(S){
for(let i=N;i>=0;i--){const w=i<4?13+i*5:34*Math.pow(1-(i-4)/(N-3),.9)+3;S[i][2]=w;
x.fillStyle='#04143c';x.beginPath();x.arc(S[i][0],S[i][1],w+2.5,0,7);x.fill();}
for(let i=N;i>=0;i--){const px=S[i][0],py=S[i][1],w=S[i][2];
x.fillStyle='hsl('+(212+i*.6)+',92%,'+(26+(1-i/N)*16)+'%)';x.beginPath();x.arc(px,py,w,0,7);x.fill();
x.fillStyle='rgba(120,220,255,.22)';x.beginPath();x.arc(px+w*.1,py+w*.38,w*.55,0,7);x.fill();
if(i>1&&i<N){const h=w*.7+8;poly([[px+6,py-w+2],[px-8,py-w-h],[px-14,py-w+3]],'#8fe3ff');}}
const t=S[N];poly([[t[0],t[1]-3],[t[0]-50,t[1]-24],[t[0]-34,t[1]],[t[0]-50,t[1]+24],[t[0],t[1]+3]],'#2a8cff');
}

function head(open){
poly([[-14,-26],[-95,-62],[-40,-12]],'#9fe4ff');
poly([[-10,-12],[-100,-18],[-40,6]],'#7fcfff');
x.save();x.translate(14,10);x.rotate(open);
poly([[-10,-2],[64,0],[70,10],[56,20],[-10,20]],'#0f4fc0');
for(let k=0;k<4;k++)poly([[12+k*12,0],[17+k*12,-9],[22+k*12,0]],'#f0ffff');
x.restore();
if(open>.15){const g=x.createRadialGradient(70,8+open*18,0,70,8+open*18,26);g.addColorStop(0,'rgba(230,255,255,.95)');g.addColorStop(1,'rgba(60,160,255,0)');x.fillStyle=g;x.fillRect(30,-10,80,60);}
poly([[-34,-4],[-10,-34],[30,-28],[86,-12],[90,0],[30,8],[-30,16]],'#1b6dff');
poly([[-10,-34],[30,-28],[86,-12],[50,-14],[10,-22]],'rgba(150,225,255,.55)');
for(let k=0;k<4;k++)poly([[36+k*13,4],[41+k*13,13],[46+k*13,4]],'#f0ffff');
x.fillStyle='#04143c';x.beginPath();x.arc(80,-6,3,0,7);x.fill();
x.save();x.shadowColor='#7ff';x.shadowBlur=22;x.fillStyle='#eaffff';x.beginPath();x.ellipse(18,-14,8,4.5,-.3,0,7);x.fill();x.restore();
}

function dragon(hx,hy,s,dir,ang,open){
const S=Array.from({length:N+1},(_,i)=>[-i*ST,Math.sin(T*3.2-i*.42)*(3+i*2.2)+Math.min(i,5)*6,0]);
x.save();x.translate(hx,hy);x.rotate(ang);x.scale(dir*s,s);
wing(S[6][0]+10,S[6][1]-4,.4,.85,'rgba(12,45,140,.75)','#3a8fe0');
body(S);
wing(S[6][0],S[6][1],0,1,'rgba(30,110,255,.55)','#8fe3ff');
head(open);
x.restore();
}

function frame(now){
const dt=Math.min(.05,(now-last)/1000);last=now;T+=dt;const k=dt*60;
const D=12,pass=Math.floor(T/D),u=(T%D)/D,dir=pass%2?-1:1;
const s=.25*Math.pow(6.5,u);
let hx=W*.12+u*(W*.88+660);if(dir<0)hx=W-hx;
const hy=Math.min(H*.62,H*(.27+.36*u))+Math.sin(T*1.6)*18*s,ang=dir*.1;
force=Math.max(0,force-k);
const fire=(u>.2&&u<.93)||force>0,open=fire?.32+.16*Math.sin(T*10):.04;
const mx=dir*s*90,my=s*(6+open*20);
const wx=hx+mx*Math.cos(ang)-my*Math.sin(ang),wy=hy+mx*Math.sin(ang)+my*Math.cos(ang);

x.save();
const sh=fire?(s-.3)*5:0;x.translate((Math.random()-.5)*sh,(Math.random()-.5)*sh);
background();
x.globalAlpha=Math.min(1,u*10);
dragon(hx,hy,s,dir,ang,open);
x.globalAlpha=1;

if(fire){
const vx=dir*Math.cos(ang),vy=dir*Math.sin(ang),n=Math.round(9*k);
for(let i=0;i<n;i++){const sp=(6+Math.random()*9)*s,q=(Math.random()-.5)*3.2*s;
P.push({x:wx,y:wy,vx:vx*sp-vy*q,vy:vy*sp+vx*q,r:(7+Math.random()*9)*s,l:0,m:38+Math.random()*34});}
if(P.length>700)P.splice(0,P.length-700);}

x.globalCompositeOperation='lighter';
if(fire){const r=260*s;x.globalAlpha=.2;x.drawImage(SPR[2],wx-r,wy-r,r*2,r*2);}
for(let i=P.length-1;i>=0;i--){const p=P[i];p.x+=p.vx*k;p.y+=p.vy*k;p.vy-=.04*s*k;p.vx*=Math.pow(.985,k);p.l+=k;
const a=p.l/p.m;if(a>=1){P.splice(i,1);continue;}
const r=p.r*(1+a*2.6);x.globalAlpha=Math.pow(1-a,.8)*.85;
x.drawImage(SPR[Math.floor(a*(SPR.length-1))],p.x-r,p.y-r,r*2,r*2);}
x.globalAlpha=1;x.globalCompositeOperation='source-over';
if(fire){x.fillStyle='rgba(40,120,255,'+.05*s+')';x.fillRect(-20,-20,W+40,H+40);}
x.restore();
requestAnimationFrame(frame);
}
requestAnimationFrame(frame);
</script>
</body>
</html>
        """;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
