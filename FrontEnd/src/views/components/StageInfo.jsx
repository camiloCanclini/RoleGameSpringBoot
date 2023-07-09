import '../../woodPattern.css'
import './stageInfo.css'
import { GameContext } from '../../GameContext';

import { useContext } from 'react';

export default function StageInfo(){
  const { room } = useContext(GameContext)
  return (
    <div className='stageInfo rounded-lg overflow-hidden shadow-2xl'>
        <div className='woodPattern p-2 '> Stage Info </div>
        <div className='relative w-min-56'>
          <ul className='z-20 p-4 gap-4 flex flex-col items-center justify-center w-full text-3xl bg-transparent'>
            <li className='z-20'><i className="fa-solid fa-fire"></i> {room.stage.heat}</li>
            <li className='z-20'><i className="fa-solid fa-droplet"></i> {room.stage.humidity}</li>
            <li className='z-20'><i className="fa-solid fa-wind"></i> {room.stage.wind}</li>
            <li className='z-20'><i className="fa-solid fa-hat-wizard"></i> {room.stage.magicBoost}</li>
          </ul>
          <img src="https://e1.pxfuel.com/desktop-wallpaper/327/350/desktop-wallpaper-medieval-times-medieval-background.jpg" alt="" className='absolute object-cover z-10 w-full h-full top-0'/>
        </div>
    </div>
  );
}