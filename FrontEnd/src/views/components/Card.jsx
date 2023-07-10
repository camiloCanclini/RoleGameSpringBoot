import './Card.css'
import '../../woodPattern.css'
import { GameContext } from '../../GameContext';

import { useContext } from 'react';


function Card ({card, cardId}){
  
  const {id,room,moveType,setMoveType, choosingMove, setChoosingMove, targetCard, settargetCard, myCard, setMyCard} = useContext(GameContext) 
  const myCardsPrefix = id == 1 ? "HC" : "GC"

  const cardHover = (e) => {
    if (choosingMove) {
      const cardHover = e.currentTarget
      const cardId = cardHover.getAttribute("card-id")
      const cardIdPrefix = cardId?.split("-").shift()
      console.log(cardHover.classList.contains("selectingMyCard"));
      if (!(cardHover.classList.contains("selectingMyCard"))) {
        if(myCardsPrefix == cardIdPrefix && myCard == null){
          cardHover.classList.add('selectingMyCard', 'myCard')
        }
        if(myCardsPrefix == cardIdPrefix && myCard != null && moveType == "DEFEND"){
          cardHover.classList.add('selectingMyCard', 'defend')
        }
        if(myCardsPrefix != cardIdPrefix && myCard != null && moveType == "SPELL"){
          cardHover.classList.add('selectingTargetCard', 'spell')
        }
        if(myCardsPrefix != cardIdPrefix && myCard != null && moveType == "HIT"){
          cardHover.classList.add('selectingTargetCard','hit')
        }
      }
    }
  }
  const cardUnHover = (e) => {
    if (choosingMove) {
      const cardHover = e.currentTarget
      const cardIdPrefix = cardHover.getAttribute("card-id")?.split("-").shift()
      cardHover.classList.remove("selectingMyCard", "selectingTargetCard", "myCard", "hit", "defend", "spell")
/*       if (cardHover.classList.contains("selectingMyCard")) {
        
        if(myCardsPrefix == cardIdPrefix && myCard == null){
          cardHover.classList.remove("selectingMyCard", 'defend')
        }
        if(myCardsPrefix != cardIdPrefix && myCard != null){
          cardHover.classList.remove("selectingTargetCard", 'hit', 'spell')
        }
      } */
    }
  }
  
  const selectCard = (e) => {
    if (choosingMove) {
      const cardSelected = e.currentTarget
      const cardIdPrefix = cardSelected.getAttribute("card-id")?.split("-").shift()
      console.log(cardSelected);
      const cardId = cardSelected.getAttribute("card-id")
      cardSelected.classList.add('selectingMyCard','myCard')
      console.log(cardId);
      if (cardIdPrefix == myCardsPrefix && myCard == null) {
        console.log('click');
        cardSelected.classList.add('selectingMyCard','myCard')
        console.log(cardSelected.classList);
        setMyCard(cardId)
      }
      if (cardIdPrefix == myCardsPrefix && myCard != null && moveType == "DEFEND") {
        cardSelected.classList.add('selectingMyCard', 'defend', "selectToDefend")
        settargetCard(cardId)
        setChoosingMove(false)
      }
      if (cardIdPrefix != myCardsPrefix && myCard != null && moveType == "SPELL") {
        settargetCard(cardId)
        cardSelected.classList.add('selectingTargetCard', 'spell')
        setChoosingMove(false)
      }
      if (cardIdPrefix != myCardsPrefix && myCard != null && moveType == "HIT") {
        settargetCard(cardId)
        cardSelected.classList.add('selectingTargetCard', 'hit')
        setChoosingMove(false)
      }
    }
  }
 

  return (
    <div card-id={cardId} className={'card flex flex-col w-40 z-10 h-72 overflow-hidden rounded-3xl border-4 border-4 relative '+ (choosingMove ? 'z-50' : null)} onClick={(e)=>{ selectCard(e)}} onMouseOver={(e)=> {cardHover(e)}} onMouseLeave={(e)=> {cardUnHover(e)}}>
      <div className='absolute top-0 right-0 bg-red-700 rounded-full w-12 h-12 z-40 flex items-center justify-center border-2 text-xl overflow-hidden'>{card.health}</div>
      <div className='absolute top-0 left-0 bg-cyan-700 rounded-full w-12 h-12 z-40 flex items-center justify-center border-2 text-3xl overflow-hidden'>{card.level}</div>
      <img src="https://e1.pxfuel.com/desktop-wallpaper/327/350/desktop-wallpaper-medieval-times-medieval-background.jpg" alt="" className='absolute object-cover z-20 w-full h-full'/>
      <img src="https://pngimg.com/d/medival_knight_PNG15928.png" alt="" className='z-30 object-contain w-full h-4/6'/>
      <div className='woodPattern z-40 grow flex p-2 flex-col'>
        <div className='d-flex'>
          <p className='text-sm text-right'>{card.typeCard}</p>
          <p className='relative bottom-2'>{card.name}</p>
        </div>
        <div className='w-full'>
          <ul className='flex justify-center items-center gap-2 w-full text-base'>
            <li><i className="fa-solid fa-hand-fist"></i> {card.strength}</li>
            <li><i className="fa-solid fa-shield-halved"></i> {card.armor}</li>
            <li><i className="fa-solid fa-bolt"></i> {card.dexterity}</li>
            <li><i className="fa-solid fa-person-running"></i> {card.speed}</li>
          </ul>
        </div>
      </div>
    </div>
  );
  
}

export default Card