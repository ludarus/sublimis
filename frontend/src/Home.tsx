import * as THREE from 'three'
import { motion } from 'motion/react'
import { useState, useRef, useEffect, Suspense } from 'react'
import { Canvas, useLoader, useFrame, useThree } from '@react-three/fiber'
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js'
import { Mesh } from 'three'
import './Home.css'
import Spam from './tools/Spam.tsx'

function handleD20Click() {
	alert("clicked")
}
function D20() {
	const glb = useLoader(GLTFLoader, 'galaxy.glb');
	const meshRef = useRef<Mesh>(null!)
	const [hovered, setHovered] = useState(false)
	useEffect(() => void (document.body.style.cursor = hovered ? 'pointer' : 'auto'), [hovered])

	useFrame((state) => {
		const t = state.clock.getElapsedTime();
		meshRef.current.rotation.y = THREE.MathUtils.lerp(
			meshRef.current.rotation.y,
			(state.pointer.x * Math.PI) - 0.3,
			0.04)
		meshRef.current.rotation.x = THREE.MathUtils.lerp(
			meshRef.current.rotation.x,
			(state.pointer.y * Math.PI * -1) + Math.atan2(meshRef.current.position.y - 0.15, 5) * 5,
			0.04)
		meshRef.current.position.y =
			THREE.MathUtils.lerp(meshRef.current.position.y, (Math.sin(t / 2) * 0.1) + 0.15, 0.1)


	})
	return (
		<mesh
			onClick={handleD20Click}
			ref={meshRef}
			//hover code taken from https://codesandbox.io/p/sandbox/q23sw?file=%2Fsrc%2FApp.js%3A27%2C58-27%2C59
			onPointerOver={(e) => (e.stopPropagation(), setHovered(true))}
			onPointerOut={(e) => (e.stopPropagation(), setHovered(false))}
			dispose={null}
		>
			<primitive object={glb.scene} />
		</mesh >
	)

}
export default function Home() {
	const [nav1, setNav1] = useState(false)




	return (
		<div id='root-container' className=''>
			<nav
				id='navbar'
				className='bg-yellow-50'
			>
				<div
					className='navbar-element'
					// onMouseEnter={() => { setNav1(true) }}
					// onMouseLeave={() => {
					// 	setNav1(false)
					// }}

					onClick={() => {
						setNav1(!nav1)
					}}
				>

					<h1>title</h1>
					<motion.div
						className="dropdown
					bg-green-500
						"
						onClick={(e) => { e.stopPropagation() }}

						animate={{
							height: nav1 ? "10em" : "0em",
						}}
						transition={{
							type: "spring", bounce: 0.45, duration: 0.5
						}}
					>

						<div className='dropdown-element' 

						>
							<a href="locallink-or-something" >dropdown thing</a>
						</div >
						<div className='dropdown-element' >
							<p>dropdown thing</p>
						</div >
						<div className='dropdown-element' >
							<p>dropdown thing</p>
						</div >
						<div className='dropdown-element' >
							<p>dropdown thing</p>
						</div >
						<div className='dropdown-element' >
							<p>dropdown thing</p>
						</div >

					</motion.div>

				</div>
				<div
					className='navbar-element
				'>
					<h1>This is a navbar element</h1>
				</div>
				<div
					className='navbar-element
					'>
					<h1>login</h1>
				</div>

			</nav >
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

