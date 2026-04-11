import * as THREE from 'three'
import { useState, useRef, useEffect } from 'react'
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js'
import { Mesh } from 'three'
import { useLoader, useFrame } from '@react-three/fiber'

function handleD20Click() {
	alert("clicked")
}
export default function D20() {
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
