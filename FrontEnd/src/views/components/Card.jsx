import './Card.css'
import '../../woodPattern.css'

function Card ({card}){

  return (
    <div className='flex flex-col overflow-hidden w-40 z-10 h-72 rounded-lg border-2 relative'>
      <img src="https://e1.pxfuel.com/desktop-wallpaper/327/350/desktop-wallpaper-medieval-times-medieval-background.jpg" alt="" className='absolute object-cover z-20 w-full h-full'/>
      <img src="https://pngimg.com/d/medival_knight_PNG15928.png" alt="" className='z-30 object-contain w-full h-4/6'/>
      <div className='woodPattern z-40 grow flex p-2 flex-col'>
        <div>
          <span className='text-sm'>Human</span>
          <span className='text-lg'>{card.name}</span>
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