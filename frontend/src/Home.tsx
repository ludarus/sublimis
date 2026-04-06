import './Home.css'

export default function Home() {
	return (
		<body>
			<nav
				id='navbar'
				className='border border-yellow-500'
			>
				<div
					className='navbar-element
border border-blue-500
				'>
					<h1>This is a navbar element</h1>
				</div>
				<div
					className='navbar-element
border border-blue-500
				'>
					<h1>This is a navbar element</h1>
				</div>
				<div
					className='navbar-element
border border-blue-500
				'>
					<h1>login</h1>
				</div>

			</nav>

			<main>
				Welcome home
			</main>
		</body>

	)
}
