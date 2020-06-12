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
  fetch('/login').then(response => response.json()).then((login) => {
    console.log(login.status);
    if (login.status) {
      fetch('/Comments').then(response => response.json()).then((com) => {
        const commentList = document.getElementById('fetch-comment');

        com.forEach((comment) => {
          commentList.appendChild(createCommentElement(comment));
        })
      });
      var str = 'Log Out';
      var link = str.link(login.logoutUrl);
      document.getElementById('login').innerHTML = link;
      console.log(login.logoutUrl);

    } else {
      var str = 'Log In';
      var link = str.link(login.loginUrl);
      document.getElementById('login').innerHTML = link;
      console.log(login.loginUrl);
    }
  });
}

function createTaskElement(comment) {
  const taskElement = document.createElement('li');

  taskElement.className = 'comment';

  const titleElement = document.createElement('span');
  titleElement.innerText = comment.title + ' - ' + comment.email;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteTask(comment);

    // Remove the task from the DOM.
    taskElement.remove();
  });

  taskElement.appendChild(titleElement);
  taskElement.appendChild(deleteButtonElement);
  return taskElement;
}


function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
function deleteTask(task) {
  const params = new URLSearchParams();
  params.append('id', task.id);
  fetch('/delete-data', {method: 'POST', body: params});
}
