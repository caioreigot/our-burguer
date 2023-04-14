import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SnackbarService {

  snackbar: Snackbar = new Snackbar();

  showMessage(message: string, isError: boolean = false) {
    if (!message) {
      message = isError 
        ? 'Ops! Erro desconhecido.'
        : 'Sucesso!';
    }

    this.snackbar.open(message, 3000, isError);
  }
}

export class Snackbar {

  snackbarContainer: HTMLElement;
  transitionDurationInMs = 400;
  timeoutIntervals: NodeJS.Timeout[] = [];
  id = 'snackbar';

  constructor() {
    const snackbar = document.createElement('div');
    snackbar.id = this.id;

    this.snackbarContainer = snackbar;
  }

  open(
    message: string,
    durationInMs: number,
    isError: boolean
  ) {
    this.removeSnackbarAndResetTimeouts();

    this.snackbarContainer.innerText = message;
    this.snackbarContainer.className = isError ? 'error' : 'success';
    this.setupAnimationStartValues();
    
    document.body.append(this.snackbarContainer);
    this.playEnterAnimation();
    
    this.timeoutIntervals.push(setTimeout(() => 
      { this.playExitAnimation() }, durationInMs
    ));
    
    this.snackbarContainer.onclick = () => this.removeSnackbarAndResetTimeouts();
  }

  private setupAnimationStartValues() {
    this.snackbarContainer.style.opacity = '0';
    this.snackbarContainer.style.transform = 'translate(-50%, 100%)';
    this.snackbarContainer.style.transition = `all ease ${this.transitionDurationInMs}ms`;
  }

  private playExitAnimation() {
    this.snackbarContainer.style.opacity = '0';
    
    setTimeout(() => {
      this.removeSnackbarAndResetTimeouts();
    }, this.transitionDurationInMs);
  }

  private playEnterAnimation() {
    return setTimeout(() => {
      this.snackbarContainer.style.opacity = '1';
      this.snackbarContainer.style.transform = 'translate(-50%, 0%)';
    }, 50);
  }

  private removeSnackbarAndResetTimeouts() {
    this.snackbarContainer.remove();
    this.timeoutIntervals.forEach(interval => clearInterval(interval));
  }
}