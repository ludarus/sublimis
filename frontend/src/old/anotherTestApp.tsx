import { GoogleOAuthProvider, GoogleLogin, type CredentialResponse } from '@react-oauth/google'
import { motion } from 'motion/react'
import { useState, useEffect } from 'react'
import './App.css'

function App() {
	const [state, setState] = useState(false)
	return (
		<body className='root-container'>
			<nav>
				<div id='nav-1' className='nav-element'>
					<a href='youtube.com'>
						icon
					</a>
				</div>
				<div id='nav-2' className='nav-element'>
					<a href='youtube.com'>
						ABOUT
					</a>
					<a href='youtube.com'>
						WORK
					</a>
				</div>
				<div id='nav-3' className='nav-element'>
					<p>s1</p>
					<p>s2</p>
					<p>s3</p>
				</div>
				<div id='nav-4' className='nav-element'>
					GET IN TOUCH
				</div>
			</nav>
			<main>

				<motion.button className='fancy-button'
					onClick={() => {
						setState(!state)
					}}
					animate={{
						scale: state ? 2 : 1,
						x: state ? "20em" : 0,
						y: state ? "10em" : 0,
						rotateZ: state ? 45 : 0
					}}
				>
					<motion.svg
						width="345"
						height="393"
						viewBox="0 0 345 393"
						fill="none"
						xmlns="http://www.w3.org/2000/svg">
						<motion.path
							d="M343.519 175.826L9.51915 391.826L0.51915 0.826202L343.519 175.826Z"
							fill="#710D6B"
							stroke="#FF0000"
							strokeWidth="10"
							animate={{
								pathLength: state ? 1 : 0,
							}}
						/>
					</motion.svg>
				</motion.button>

			</main>
		</body>
	)

}

export default App
