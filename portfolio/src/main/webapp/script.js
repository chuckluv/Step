// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomFunFact() {
  const funFacts = [
    'Favorite TV show is Game of Thrones', 'Favorite Sport is Football',
    'I have fractured every finger on my left hand',
    'I have played the double bass since Third Grade'
  ];

  // Pick a random greeting.
  const funFact = funFacts[Math.floor(Math.random() * funFacts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fun-fact-container');
  factContainer.innerText = funFact;
}

 function getComments() {

    fetch('/data').then(response => response.json()).then((com) => {
  //console.log(JSON.stringify(com.comments))

   
  const commentList =document.getElementById('fetch-comment');
  commentList.innerHTML = " ";
  commentList.appendChild(
      createListElement( 'Comment: '+ com.comment1));
    });
 }
/*
async function getComments() {
  const response = await fetch('/data');
  const quote = await response.json();
  const commentList =document.getElementById('fetch-comment');
  commentList.appendChild(
      createListElement("People"));
    
  }
*/
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
