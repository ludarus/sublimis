import { Suspense } from 'react'
import { Canvas } from '@react-three/fiber'

import './Home.css'
import D20 from './Model.tsx'
import NavBar from './Nav.tsx'
import Spam from './tools/Spam.tsx'


export default function Home() {
	return (
		<div id='root-container' className=''>

			<NavBar />

			<main className=''>
				<div className='rendering-space '>
					<Canvas
						camera={{ position: [0, 0, 5] }}
					>
						<Suspense fallback={null}>
							<D20 />
						</Suspense>
						<ambientLight
							intensity={5}
						/>
					</Canvas>
				</div>
			</main>
		</div >

	)
}

