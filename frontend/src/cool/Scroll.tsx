import React from "react";
import { useState } from 'react'
import { motion } from 'motion/react'
import './Scroll.css'

type ScrollElementProps = {
	children: React.ReactNode | null
}

export function ScrollElement({ children }: ScrollElementProps) {
	const [hovered, setHovered] = useState(false)

	return (
		<div
			onMouseEnter={() => setHovered(true)}
			onMouseLeave={() => { setHovered(false) }}
			className="scroll-dropdown-element "
		>

			<motion.svg height="0.6em" className="" viewBox="0 0 121 121" fill="none" animate={{ opacity: hovered ? 1 : 0 }} transition={{ duration: 0.2, ease: "linear" }}>
				<g filter="url(#filter0_g_54_38)">
					<path d="M60.1029 5.8C64.8259 16.2037 72.5436 27.2881 82.731 37.4755C92.9181 47.6626 104.002 55.3799 114.406 60.1029C104.002 64.8259 92.9181 72.5432 82.731 82.7303C72.5436 92.9177 64.8259 104.002 60.1029 114.406C55.3799 104.002 47.6634 92.9175 37.4762 82.7303C27.2887 72.5428 16.2038 64.8259 5.8 60.1029C16.2038 55.3799 27.2887 47.663 37.4762 37.4755C47.6634 27.2883 55.3799 16.2036 60.1029 5.8Z" fill="#1C1103" />
				</g>
				<defs>
					<filter id="filter0_g_54_38" x="0" y="0" width="120.206" height="120.206" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
						<feFlood flood-opacity="0" result="BackgroundImageFix" />
						<feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape" />
						<feTurbulence type="fractalNoise" baseFrequency="0.25641024112701416 0.25641024112701416" numOctaves="3" seed="1614" />
						<feDisplacementMap in="shape" scale="11.600000381469727" xChannelSelector="R" yChannelSelector="G" result="displacedImage" width="100%" height="100%" />
						<feMerge result="effect1_texture_54_38">
							<feMergeNode in="displacedImage" />
						</feMerge>
					</filter>
				</defs>
			</motion.svg>
			<div style={{ padding: "0em 0em 0em 0.5em" }}>{children}</div>
		</div>
	)
}

type ScrollProps = {
	children: React.ReactNode | null
	title: string
	pic?: string | null
}

export default function Scroll({ children, title, pic = null }: ScrollProps) {

	const [hovered, setHovered] = useState(false)
	const [clicked, setClicked] = useState(false)
	return (
		<div className="scroll-main"
			onMouseEnter={() => { setHovered(true) }}
			onMouseLeave={() => { setHovered(false) }}
			onClick={() => { setClicked(!clicked) }}
		>
			<div className="scroll-top">
				<svg className="scroll-left" width="1.5em" height="2em" viewBox="0 0 90 120" fill="none">
					<ellipse cx="44.9952" cy="60.0001" rx="44.9029" ry="59.931" transform="rotate(-0.088276 44.9952 60.0001)" fill="#6C6247" />
					<path d="M48.4117 15.0466C37.4247 15.0636 26.8961 20.6047 19.1422 30.451C11.3883 40.2973 7.0442 53.6422 7.06561 67.5501C7.08701 81.4579 11.4721 94.7893 19.2563 104.612C27.0405 114.434 37.5861 119.943 48.5732 119.926L48.4924 67.4862L48.4117 15.0466Z" fill="#D3C296" />
					<path d="M83.6534 45.9136C80.9837 37.4157 76.7897 30.0935 71.5033 24.7013C66.2169 19.3091 60.0293 16.0418 53.5779 15.2361C47.1264 14.4304 40.6443 16.1154 34.7994 20.1174C28.9545 24.1194 23.958 30.2938 20.3247 38.0046C16.6914 45.7154 14.5526 54.6837 14.1286 63.9858C13.7046 73.2879 15.0108 82.5874 17.9125 90.9264C20.8143 99.2654 25.2068 106.342 30.6375 111.428C36.0681 116.514 42.3407 119.425 48.809 119.862L50.4717 67.4795L83.6534 45.9136Z" fill="#6C6247" />
				</svg>
				<svg className="scroll-right" width="1.5em" height="2em" viewBox="0 0 90 120" fill="none">
					<ellipse cx="45" cy="60" rx="45" ry="60" transform="rotate(-0.088276 45 60)" fill="#D3C296" />
				</svg>
			</div>

			<motion.div className='scroll-title'
			>
				{pic ?
					//if has picture
					<div style={{padding:"0.25em 0em"}}>
						<img src={pic} />
					</div>
					:
					//if doesn[t have picture
					<h1>{title}</h1>
				}
			</motion.div>
			<motion.div className='scroll-dropdown'
				transition={{
					type: "tween", duration: 1, ease: "easeOut"
				}}
				animate={{ height: hovered ? "auto" : "0em" }}

			>
				{pic ?
					//if has pic
					<h1 style={{ fontSize: "1.5em" }}>{title}</h1>
					:
					//if doesnt have pic
					null
				}
				{children}
			</motion.div>

			<div className="scroll-bottom">
				<svg className="scroll-right" width="1.5em" height="2em" viewBox="0 0 90 120" fill="none">
					<ellipse cx="45" cy="60" rx="45" ry="60" transform="rotate(-0.088276 45 60)" fill="#D3C296" />
				</svg>
				<svg className="scroll-left" width="1.5em" height="2em" viewBox="0 0 90 120" fill="none" transform="scale(1 -1)">
					<ellipse cx="44.9952" cy="60.0001" rx="44.9029" ry="59.931" transform="rotate(-0.088276 44.9952 60.0001)" fill="#6C6247" />
					<path d="M48.4117 15.0466C37.4247 15.0636 26.8961 20.6047 19.1422 30.451C11.3883 40.2973 7.0442 53.6422 7.06561 67.5501C7.08701 81.4579 11.4721 94.7893 19.2563 104.612C27.0405 114.434 37.5861 119.943 48.5732 119.926L48.4924 67.4862L48.4117 15.0466Z" fill="#D3C296" />
					<path d="M83.6534 45.9136C80.9837 37.4157 76.7897 30.0935 71.5033 24.7013C66.2169 19.3091 60.0293 16.0418 53.5779 15.2361C47.1264 14.4304 40.6443 16.1154 34.7994 20.1174C28.9545 24.1194 23.958 30.2938 20.3247 38.0046C16.6914 45.7154 14.5526 54.6837 14.1286 63.9858C13.7046 73.2879 15.0108 82.5874 17.9125 90.9264C20.8143 99.2654 25.2068 106.342 30.6375 111.428C36.0681 116.514 42.3407 119.425 48.809 119.862L50.4717 67.4795L83.6534 45.9136Z" fill="#6C6247" />
				</svg >
			</div>
		</div>
	)
}
