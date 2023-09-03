import "./FinisihScreen.css";
import "../../../woodPattern.css";
import { GameContext } from "../../../GameContext";
import trophyImg from "../../../assets/images/trophy.svg"
import { useContext, useEffect, useState } from "react";

export default function ResultPopUp() {
  const { resultFinish, setResultFinish, room } = useContext(GameContext);

  return (
    <>
      <div className="absolute flex flex-col items-center justify-center z-50 text-6xl mb-8 w-full h-full backdrop-blur-md">
        <div className="woodPattern w-3/5 p-12 rounded-2xl border-4 flex flex-col justify-evenly items-center">
          <h1 className="text-center">Result Of The Game</h1>
          <p className="text-4xl p-5">{resultFinish.message}</p>
          <div className="flex w-full px-8 justify-evenly p-4 text-center">
            <div className="text-2xl flex flex-col items-center">
              <img src={trophyImg} alt="" className="w-2/3"/>
            </div>
          </div>
          <button
            className="border-4 p-3 rounded-3xl my-2 w-2/5 transition-all hover:scale-105"
            onClick={() => window.history.go(-1)}
          >
            Close
          </button>
        </div>
      </div>
      <div className="z-40 absolute w-full h-full bg-black opacity-80 flex flex-col justify-center items-center "></div>
    </>
  );
}
