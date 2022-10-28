package com.isu.jetnoteapp.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.jetnoteapp.data.NotesDataSource
import com.isu.jetnoteapp.model.Note
import com.isu.jetnoteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.getAllNotes().distinctUntilChanged()
                .collect{listOfNotes->
                    if(listOfNotes.isNullOrEmpty()){
                        Log.d("Room DB Statue", "Empty: NoteTable")
                        _noteList.value = emptyList()
                    }
                    else
                        _noteList.value = listOfNotes
                }
        }
    }
    fun addNote(note : Note) = viewModelScope.launch { noteRepository.addNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { noteRepository.updateNote(note) }
    fun removeNote(note: Note) = viewModelScope.launch { noteRepository.deleteNote(note) }

}