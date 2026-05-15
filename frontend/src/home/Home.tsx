import { Suspense, useEffect, useState } from 'react'
import { Canvas } from '@react-three/fiber'
// import Aurora from '../bg/Aurora.tsx'

import './Home.css'
import D20 from '../3d/Model.tsx'
import NavBar from './Nav.tsx'

import type { SublimisUser } from '../types/SublimisUser.tsx'
// import Spam from './tools/Spam.tsx'


export default function Home() {
	const [userInfo, setInfo] = useState<SublimisUser | null>(null)

	useEffect(() => {
		const fetchData = async () => {
			const res = await fetch(
				'http://localhost:9000/getUserInfo',
				{
					method: 'GET',
					credentials: 'include'
				}
			)
			setInfo(await res.json())
		};

		fetchData();
	}, []); // runs once on page load

	return (
		<div id='root-container' >
			{
				// <div id='bg-container'>
				// 	<Aurora
				// 		colorStops={["#7cff67", "#B497CF", "#5227FF"]}
				// 		blend={0.5}
				// 		amplitude={1.0}
				// 		speed={0.4}
				// 	/>
				// </div>
			}

			<NavBar setInfo={setInfo} userInfo={userInfo} />

			<main className=''>
				<div className='rendering-space '>
					<Canvas
						camera={{ position: [0, 0, 5] }}
					>
						<Suspense fallback={"loading..."}>
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
