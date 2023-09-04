# RoleGameSpringBoot

## Summary
This is my final project for "Programacion III", It's was my first project using SpringBoot Framework, also it was my first time developement a Websocket Application. Basically is a Role Game inspired on Magic, the popular card game. How I said, It is a final project of one of a subjects that I studied in this year, on the university. It was intended to practice the Java Language, and make a Aplication that include the mainly concepts of OOP, polymorphism, abstraction, encapsulaton and inherance.

I decided to go more further with this application, the objetive was make a role game, like Magic, that can be able to run through console. But the teacher allowed us to blow our imagination. So that's the reason because I decided to implement an entire Web Application. It's been a while I was waiting for an idea/project to implement React and some other features...that day came!

In conclusion this Web Application has a Frontend developement with React, and I used: Axios, SockJs, StompJs, React-Router, and SweetAlert Libraries. One thing to keep in mind is that i build the Front Client using React and ViteJs, if you dont know it, ViteJs is like WebPack, it compress and optimize all the code of the frontend, so you need to intall it to run the dev mode. And, in the other hand, we have a Backend development in Java using Springboot Framework, Lombook, WebSocket library, and other features.

## Installation

### Frontend Client 

To Install the client we need Node install in our Pc.

![nodejs](https://cdn.iconscout.com/icon/free/png-256/free-node-js-1174925.png?f=webp)

[https://nodejs.org/es/download](https://nodejs.org/es/download)

Once we have Node installed. Enter in to the `Frontend` folder and open a console or terminal inside and type the follow command
```bash
npm
 install
```
With that command the Node Package Manager will install the dependencies of the Frontend Client. Once it finish we can run the client service with the following command

```bash
npm run dev
```

If everything was okey, you should see a message that shows you the url of the client. By default, since we are using Vite, It opens the service on his default, It should be the 5173 port, if it doesn't, then kill the actual process (that we run) touching `Ctrl`+`C`, or `Ctrl`+`X`, or `Ctrl`+`Z` until it stops. Once we kill the previous process we can put the following command:

```bash
npm run dev -- --port 8000
```

*You should do that in case that the port isn´t the 5173 port.*
