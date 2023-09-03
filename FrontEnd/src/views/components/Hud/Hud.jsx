import './Hud.css'
import '../../../woodPattern.css'
import woodPattern from '../../../assets/pattern/wood.jpg'
import Card from '../Card/Card'
import {GameContext} from '../../../GameContext';
import { useContext } from 'react'



function Hud ({position, cleanMove}){

  console.log(useContext(GameContext));
  const {id,room,moveType,setMoveType,setChoosingMove, targetCard, settargetCard, myCard, setMyCard} = useContext(GameContext)
  
  

  const Base = ({player, cardsId})=>{

    return (
      <div className='woodPattern object-cover h-36 flex items-center'>
      {
      position == "bottom" 
      ? 
        <div className='w-1/2 flex gap-4 p-8'>
          <button className='z-30 transition-all hover:scale-105 grow basis-0 p-4 cursor-pointer border-4 shadow-red-600 shadow-md border-red-400 rounded-xl bg-red-600' onClick={()=>{
            setMyCard(null)
            settargetCard(null)
            setChoosingMove(true)
            setMoveType("HIT")
            cleanMove()
          }}>Hit</button>
          <button className='z-30 transition-all hover:scale-105 grow basis-0 p-4 cursor-pointer border-4 shadow-green-600 border-green-400 shadow-md rounded-xl bg-green-500' onClick={()=>{
            setMyCard(null)
            settargetCard(null)
            setChoosingMove(true)
            setMoveType("DEFEND")
            cleanMove()
          }}>Defend</button>
          <button className='z-30 transition-all hover:scale-105 grow basis-0 p-4 cursor-pointer border-4 shadow-cyan-600 border-cyan-400 shadow-md rounded-xl bg-cyan-600' onClick={()=>{
            setMyCard(null)
            settargetCard(null)
            setChoosingMove(true)
            setMoveType("SPELL")
            cleanMove()
          }}>Cast Spell</button>
        </div>
      :
      null
      }
          <div className={(position == 'bottom'? 'absolute bottom-0 mb-20 pr-8 justify-end' : 'absolute top-0 mt-20 pl-8 justify-start')+' w-full flex  gap-8'}>
            {
              player.cards.map((item, index) => item.health > 0 ? <Card position={position} key={index} card={item} cardId={cardsId+index}/> : null)
            }
          </div>
      </div>
    );
  }

  return (
    <>
      {position == 'bottom' ?
      <div className="absolute bottom-0 w-full">
        <div className='text-4xl absolute bottom-5 right-8'>{(id == 1 ? room.hostPlayer.namePlayer : room.guestPlayer.namePlayer )}</div>
        <img src={woodPattern} alt="" className='wave-bottom h-40 w-full object-cover'/> 
        <Base pos={position} player={(id == 1 ? room.hostPlayer : room.guestPlayer)} cardsId={id == 1 ? "HC-" : "GC-"}></Base>
      </div>
      :
      <div className="absolute top-0 w-full">
        <Base pos={position} player={(id == 1 ? room.guestPlayer : room.hostPlayer)} cardsId={id == 1 ? "GC-" : "HC-"}></Base>
        <img src={woodPattern} alt="" className='wave-top h-40 w-full object-cover'/> 
        <div className='text-4xl absolute top-5 left-8'>{(id == 1 ? room.guestPlayer.namePlayer : room.hostPlayer.namePlayer )}</div>
      </div>
      }
    </>
  );
  
}

export default Hud