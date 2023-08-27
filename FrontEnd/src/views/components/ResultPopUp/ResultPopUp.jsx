import './ResultPopUp.css'
import '../../../woodPattern.css'
import {GameContext} from '../../../GameContext';
import Card from '../Card/Card';
import { useContext, useEffect, useState } from 'react'

export default function ResultPopUp (){
  
  const { result, setResult, room } = useContext(GameContext)
  const [winnerCard, setWinnerCard] = useState(null)
  const [loserCard, setLoserCard] = useState(null)

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
    console.log(result.data.whoWins);
    if (result.data.whoWins == 1) {
      setWinnerCard(searchCard(result.data.hostResult.cardUsedId))
      setLoserCard(searchCard(result.data.guestResult.cardUsedId))
    }
    if (result.data.whoWins == 2) {
      setWinnerCard(searchCard(result.data.guestResult.cardUsedId))
      setLoserCard(searchCard(result.data.hostResult.cardUsedId))
    }
    if (result.data.whoWins == 3) {
      setWinnerCard(searchCard(result.data.hostResult.cardUsedId))//Si empatan el host se considera como winnerCard
      setLoserCard(searchCard(result.data.guestResult.cardUsedId))
    }
  },[])

  return (
    <>
      <div className="absolute flex flex-col items-center justify-center z-50 text-6xl mb-8 w-full h-full backdrop-blur-md"> 
      {winnerCard != null && loserCard != null ?
        <div className='woodPattern w-3/5 p-12 rounded-2xl border-4 flex flex-col justify-evenly items-center'>
          <h1 className='text-center'>Result Of The Movements</h1>
          <p className='text-4xl p-5'>{result.message}</p>
          <div className='flex w-full px-8 justify-evenly p-4 text-center'>
            <div className='text-2xl flex flex-col items-center'>
              {
              result.data.whoWins != 3 ? 
              <>
                <p>{result.data.whoWins == 1? room.hostPlayer.namePlayer : room.guestPlayer.namePlayer }</p>
                <p>Movement: {result.data.whoWins == 1? result.data.hostResult.moveType : result.data.guestResult.moveType }</p>
                <p className='p-2 text-4xl'>Winner Card</p>
                
              </>
              :
              <>
                <p>{room.hostPlayer.namePlayer}</p>
                <p>Movement: {result.data.hostResult.moveType}</p>  
              </> 
              }
              <Card cardId={0} card={winnerCard}></Card>
              {
                result.data.whoWins == 3 ?
                <p className='py-4'>Damage Dealed: {result.data.hostResult.damageDone}</p>
                :
                <p className='py-4'>
                  Damage Dealed: {result.data.whoWins == 1 ? result.data.hostResult.damageReceived : result.data.guestResult.damageReceived}
                </p>
              }
            </div>
            <div className='text-2xl flex flex-col items-center'>
              {result.data.whoWins != 3 ? 
              <>
                <p>{result.data.whoWins == 1? room.guestPlayer.namePlayer : room.hostPlayer.namePlayer }</p>
                <p>Movement: {result.data.whoWins == 1? result.data.guestResult.moveType : result.data.hostResult.moveType }</p>
                <p className='p-2 text-4xl'>Loser Card</p> 
              </>
                :
              <>
                <p>{room.guestPlayer.namePlayer}</p>
                <p>Movement: {result.data.guestResult.moveType}</p>  
              </> 
              }
              <Card cardId={1} card={loserCard}></Card>
              {
                result.data.whoWins == 3 ?
                <p className='py-4'>Damage Dealed: {result.data.guestResult.damageDone}</p>
                :
                <p className='py-4'>
                  Damage Recived: {result.data.whoWins == 1 ? result.data.guestResult.damageReceived : result.data.hostResult.damageReceived}
                </p>
              }
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