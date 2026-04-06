
//random shit::
function PostButton() {
	async function postData(msg: String) {
		await fetch(
			'http://localhost:9000/dump',
			{
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(msg),
				credentials: 'include'
			}
		).then(response => console.log(response))
		// .then(data => console.log(data))
		// .catch(error => console.log(error))
	}
	return (
		<div>
			<button onClick={() => postData("this is a message")}
				className="outline-2 bg-white">
				post something
			</button>
		</div>
	)
}

export default PostButton
