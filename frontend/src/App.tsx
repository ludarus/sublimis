import { useState } from 'react'
// import reactLogo from './assets/react.svg'
// import viteLogo from './assets/vite.svg'
// import heroImg from './assets/hero.png'
import './App.css'



function App() {
	// const [count, setCount] = useState(0)
	var img: String = "StringVariable"
	var loggedIn: boolean = true

	const [count, setCount] = useState(0);
	function handleClick() {
		// alert('you clkced me')
		setCount(count + 1);
	}
	return (
		<div>
			<h1>Welcome, </h1>
			<p>This is is the sample text for Project sublimis, a DND hosting platform. </p>
			<br />
			<LoginButton logged={loggedIn} />

		</div>
	)
}

type LoginButtonProps = {
	logged: boolean;
}
function LoginButton({ logged }: LoginButtonProps) {
	return (
		<div>
			<button onClick={function() { alert('hi') }} 
			className="
			rounded-2xl 
			outline-1 
			outline-black
			">
				{logged ? <p>Welcome back!</p> : <p>Please sign in.</p>}
			</button>
		</div>
	)
}
export default App
