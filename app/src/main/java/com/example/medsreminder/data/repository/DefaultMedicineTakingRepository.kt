package com.example.medsreminder.data.repository

import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.MedicineTaking
import com.example.medsreminder.model.Response
import com.example.medsreminder.model.Taking
import com.example.medsreminder.util.createTakings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultMedicineTakingRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : MedicineTakingRepository {


    /* This will save a new MedicineTaking in Firestore, if there is no internet connection a loading
    * state will be sent, when there is internet connection again and the task completes a response will be
    * sent. */
    override fun saveMedicineTaking(medicine: Medicine): Flow<Response<Boolean>> = flow {

        emit(Response.Loading)

        val medicineTaking = MedicineTaking(medicine = medicine, takings = createTakings(medicine))

        try {
            firestore.collection(firebaseAuth.uid!!).document(medicine.name).set(medicineTaking)
                .await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e.message.toString()))
        }
    }

    override fun getMedicineTakings(): Flow<List<MedicineTaking>> {
        val docRef = firestore.collection(firebaseAuth.uid!!)

        return docRef.snapshots().map {
            it.toObjects()
        }

    }

    /* Firestore doesn't allow to edit one element inside the array, so first delete the item and then
    * insert the updated item*/
    override fun updateTaking(taking: Taking, status: MedicineStatusEnum){
        val newTaking = taking.copy(status = status.name)
        val ref = firestore.collection(firebaseAuth.uid!!).document(taking.medicineName)

        // First delete
        ref.update("takings", FieldValue.arrayRemove(taking))

        // then insert updated taking
        ref.update("takings", FieldValue.arrayUnion(newTaking))
    }

}
