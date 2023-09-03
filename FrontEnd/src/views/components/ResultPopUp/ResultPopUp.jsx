import './ResultPopUp.css'
import '../../../woodPattern.css'
import {GameContext} from '../../../GameContext';
import Card from '../Card/Card';
import { useContext, useEffect, useState } from 'react'

export default function ResultPopUp (){
  
  const { result, setResult, room } = useContext(GameContext)
  const [cardUsedHost, setCardUsedHost] = useState(null)
  const [targetCardHost, setTargetCardHost] = useState(null)
  const [cardUsedGuest, setCardUsedGuest] = useState(null)
  const [targetCardGuest, setTargetCardGuest] = useState(null)
  const [hostDamageRecived, setHostDamageRecived] = useState(null)
  const [hostDamageDone, setHostDamageDone] = useState(null)
  const [guestDamageRecived, setGuestDamageRecived] = useState(null)
  const [guestDamageDone, setGuestDamageDone] = useState(null)

  const searchCard = (cardId)=>{
      const cardPrefix = cardId.split("-")[0]
      const cardNumber = parseInt(cardId.split("-")[1])
      if (cardPrefix == "HC") {
        return room.hostPlayer.cards[cardNumber]
      }else{
        return room.guestPlayer.cards[cardNumber]
      }
  }
  

  useEffect(()=>{
    
    console.log(result);
    //console.log(result.data.whoWins);
    
    setCardUsedHost(searchCard(result.data.hostResult.cardUsedId))
    setTargetCardHost(searchCard(result.data.hostResult.targetCardId))
    setCardUsedGuest(searchCard(result.data.guestResult.cardUsedId))
    setTargetCardGuest(searchCard(result.data.guestResult.targetCardId))
    setHostDamageRecived(result.data.hostResult.damageReceived)
    setHostDamageDone(result.data.hostResult.damageDone)
    setGuestDamageRecived(result.data.guestResult.damageReceived)
    setGuestDamageDone(result.data.guestResult.damageDone)

  },[])

  return (
    <>
      <div className="absolute flex flex-col items-center justify-center z-50 text-6xl mb-8 w-full h-full backdrop-blur-md"> 
      {cardUsedHost != null && targetCardHost != null && cardUsedGuest != null && targetCardGuest != null ?
        <div className='woodPattern w-3/5 p-12 rounded-2xl border-4 flex flex-col justify-evenly items-center'>
          <h1 className='text-center'>Result Of The Movements</h1>
          <p className='text-4xl p-5'>{result.message}</p>
          <div className='flex w-full px-8 justify-evenly p-4 text-center'>
            <div className='text-2xl flex flex-col items-center'>
              <p>Player: {room.hostPlayer.namePlayer}</p>
              <p>Movement: {result.data.hostResult.moveType}</p>
              {
              result.data.whoWins == 1?
                <p className='p-2 text-4xl'>WINNER</p>
              :
              null
              }
              {
              result.data.whoWins == 2?
                <p className='p-2 text-4xl'>LOSER</p>
              :
              null
              }
              <div className='flex gap-2 p-2'>
                <Card cardId={0} card={cardUsedHost}></Card>
                <Card cardId={1} card={targetCardHost}></Card>
              </div>
              <p> { cardUsedHost.name } to { targetCardHost.name }</p>
              <p className='py-4'>Damage Done: {hostDamageDone}</p>
              <p className='py-4'>Damage Received: {hostDamageRecived}</p>
            </div>

            <div className='text-2xl flex flex-col items-center'>
              <p>Player: {room.guestPlayer.namePlayer}</p>
              <p>Movement: {result.data.guestResult.moveType}</p>
              {
              result.data.whoWins == 1?
                <p className='p-2 text-4xl'>LOSER</p>
              :
              null
              }
              {
              result.data.whoWins == 2?
                <p className='p-2 text-4xl'>WINNER</p>
              :
              null
              }
              <div className='flex gap-2 p-2'>
                <Card cardId={2} card={cardUsedGuest}></Card>
                <Card cardId={3} card={targetCardGuest}></Card>
              </div>
              <p> { cardUsedGuest.name } to { targetCardGuest.name }</p>
              <p className='py-4'>Damage Done: {guestDamageDone}</p>
              <p className='py-4'>Damage Received: {guestDamageRecived}</p>
            </div>
          </div>
          <button className='border-4 p-3 rounded-3xl my-2 w-2/5 transition-all hover:scale-105' onClick={()=>setResult(null)}>Close</button>
        </div>
        :
        null}
      </div>
      <div className="z-40 absolute w-full h-full bg-black opacity-80 flex flex-col justify-center items-center ">
      </div>
    </>
  );
  
}