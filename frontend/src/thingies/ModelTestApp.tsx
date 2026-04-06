import { motion } from 'motion/react'
import { useState, useEffect } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { useLoader } from '@react-three/fiber'
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js'

import './App.css'
import { BoxGeometry } from 'three'

function App() {

	function Scene() {
		const gltf = useLoader(GLTFLoader, 'scene.gltf');
		return <primitive object={gltf.scene} />
	}

	return (
		<body>
			<div className='idiot'>
				<Canvas
				camera={{ position: [100, 100, 100] }}
				>
					<mesh
						rotation={[0.3, Math.PI / 3, 0]}
					>
						<boxGeometry args={[2, 1, 1]} />
						<meshStandardMaterial />
						<Scene/>
					</mesh>
					<ambientLight intensity={1} />
					<directionalLight color="red" position={[0, 0, 5]} />
				</Canvas>
			</div>
		</body>
	)
}

export default App
