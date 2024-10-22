import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonInput,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar
} from '@ionic/react';
import { getLogger } from '../core';
import { MovieContext } from './MovieProvider';
import { RouteComponentProps } from 'react-router';
import { MovieProps } from './MovieProps';

const log = getLogger('MovieEdit');

interface MovieEditProps extends RouteComponentProps<{
  id?: string;
}> {}

const MovieEdit: React.FC<MovieEditProps> = ({ history, match }) => {
  const { movies, saving, savingError, saveMovie } = useContext(MovieContext);
  const [title, setTitle] = useState('');
  const [director, setDirector] = useState('');
  const [year, setYear] = useState<number>(0);
  const [rating, setRating] = useState<number>(0);

  const [movie, setMovie] = useState<MovieProps>();

  useEffect(() => {
    const routeId: string | undefined = match.params.id || '';
    log('useEffect', match.params.id, movies, movies?.find(it => it.id == routeId));
    const movie = movies?.find(it => it.id == routeId);
    setMovie(movie);
    if (movie) {
      setTitle(movie.title);
      setDirector(movie.director);
      setYear(movie.year);
      setRating(movie.rating);
    }
  }, [match.params.id, movies]);

  const handleSave = useCallback(() => {
    const editedMovie = movie ? { ...movie, title, director, year, rating } :
        { title, director, year, rating };
    saveMovie && saveMovie(editedMovie).then(() => history.goBack());
  }, [movie, saveMovie, title, director, year, rating, history]);
  log('render');
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="tertiary">
          <IonTitle>Edit</IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={handleSave} color="secondary">
              Save
            </IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')} />
        <IonInput value={director} onIonChange={e => setDirector(e.detail.value || '')} />
        <IonInput value={year} onIonChange={e => setYear(parseInt(e.detail.value || '0'))} />
        <IonInput value={rating} onIonChange={e => setRating(parseInt(e.detail.value || '0'))} />
        <IonLoading isOpen={saving} />
        {savingError && (
          <div>{savingError.message || 'Failed to save movie'}</div>
        )}
      </IonContent>
    </IonPage>
  );
};

export default MovieEdit;
